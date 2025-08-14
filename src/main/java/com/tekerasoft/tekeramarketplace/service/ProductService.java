package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.ProductUiDto;
import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.exception.StockException;
import com.tekerasoft.tekeramarketplace.exception.UnauthorizedException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.document.Cart;
import com.tekerasoft.tekeramarketplace.model.document.CartAttributes;
import com.tekerasoft.tekeramarketplace.model.document.CartItem;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItemType;
import com.tekerasoft.tekeramarketplace.repository.jparepository.*;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import com.tekerasoft.tekeramarketplace.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final SellerRepository sellerRepository;
    private final VariationRepository variationRepository;
    private final SearchItemService searchItemService;
    private final SellerService sellerService;
    private final AuthenticationFacade  authenticationFacade;

    public ProductService(ProductRepository productRepository,
                          FileService fileService,
                          CategoryRepository categoryRepository,
                          SubCategoryRepository subCategoryRepository,
                          SellerRepository sellerRepository,
                          VariationRepository variationRepository, SearchItemService searchItemService,
                          SellerService sellerService,
                          AuthenticationFacade authenticationFacade) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.sellerRepository = sellerRepository;
        this.variationRepository = variationRepository;
        this.searchItemService = searchItemService;
        this.sellerService = sellerService;
        this.authenticationFacade = authenticationFacade;
    }

    public Product getById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    @Transactional
    public ApiResponse<?> create(CreateProductRequest req, List<MultipartFile> images) {
        try {
            Product product = new Product();
            product.setName(req.getName());
            product.setSlug(SlugGenerator.generateSlug(req.getName()));
            product.setCode(req.getCode());
            product.setBrandName(req.getBrandName());
            product.setDescription(req.getDescription());
            product.setCurrencyType(req.getCurrencyType());
            product.setProductType(req.getProductType());
            product.setTags(req.getTags());
            product.setAttributes(req.getAttributeDetails());
            product.setActive(true);

            // Company
            Seller seller = sellerRepository.findById(UUID.fromString(req.getCompanyId()))
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            product.setSeller(seller);
            seller.getProducts().add(product);

            // Category
            Category category = categoryRepository.findById(UUID.fromString(req.getCategoryId()))
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // SubCategories
            Set<SubCategory> subCategories = req.getSubCategories().stream()
                    .map(id -> subCategoryRepository.findById(UUID.fromString(id))
                            .orElseThrow(() -> new RuntimeException("SubCategory not found: " + id)))
                    .collect(Collectors.toSet());
            product.setSubCategories(subCategories);

            // Variations
            List<Variation> variations = new ArrayList<>();
            for (VariationRequest varReq : req.getVariants()) {
                Variation var = new Variation();
                var.setModelName(varReq.getModelName());
                var.setModelCode(varReq.getModelCode());
                var.setProduct(product);
                var.setColor(varReq.getColor());

                // Attributes
                List<Attribute> variationAttributes = varReq.getAttributes().stream()
                        .map(attr -> new Attribute(
                                attr.getPrice(),
                                attr.getDiscountPrice(),
                                attr.getStock(),
                                attr.getMaxPurchaseStock(),
                                attr.getSku(),
                                attr.getBarcode(),
                                attr.getAttributeDetails(),
                                var
                        )).collect(Collectors.toList());
                var.setAttributes(variationAttributes);

                // Görseller (galeri + multipart)
                List<String> finalImageUrls = new ArrayList<>();

                // 1. Galeriden seçilen görseller
                if (varReq.getImageUrls() != null && !varReq.getImageUrls().isEmpty()) {
                    finalImageUrls.addAll(varReq.getImageUrls());
                }

                // 2. Multipart olarak yüklenen görseller
                if (images != null && !images.isEmpty()) {
                    for (MultipartFile image : images) {
                        Map<String, String> parsed = parseImageFileName(image.getOriginalFilename());
                        if (parsed == null) continue;

                        String imageModelCode = parsed.get("modelCode");
                        String imageColor = parsed.get("color");

                        if (varReq.getModelCode().equalsIgnoreCase(imageModelCode)
                                && varReq.getColor().contains(imageColor)) {

                            String imageUrl = fileService.productFileUpload(
                                    image,
                                    seller.getSlug(),
                                    SlugGenerator.generateSlug(req.getName()),
                                    imageColor
                            );
                            finalImageUrls.add(imageUrl);
                        }
                    }
                }

                // Varyasyona görselleri ata
                var.setImages(finalImageUrls);

                variations.add(var);
            }

            product.setVariations(variations);

            // Video
            if (req.getVideoUrl() != null) {
                if (req.getVideoUrl().startsWith("/temp")) {
                    String newPath = req.getVideoUrl().replace("temp/", "products/");
                    fileService.copyObject(req.getVideoUrl(), newPath);
                    product.setVideoUrl(newPath);
                    fileService.deleteInFolderFile(req.getVideoUrl());
                } else {
                    product.setVideoUrl(req.getVideoUrl());
                }
            }

            productRepository.save(product);

            return new ApiResponse<>("Product Created", HttpStatus.CREATED.value());

        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    @Transactional
    public ApiResponse<?> update(UpdateProductRequest req, List<MultipartFile> images) {
        Seller checkSeller = sellerService.getSellerByUserId(authenticationFacade.getCurrentUserId());

        try {
            // Ürün bulunuyor
            Product product = productRepository.findById(UUID.fromString(req.getId()))
                    .orElseThrow(() -> new NotFoundException("Product not found: " + req.getId()));
            if(!Objects.equals(product.getSeller().getId(), checkSeller.getId())) {
                throw new UnauthorizedException("Bu ürünü güncellemek için yetkiniz yok !");
            }
            // Temel alanlar
            product.setName(req.getName());
            product.setCode(req.getCode());
            product.setBrandName(req.getBrandName());
            product.setDescription(req.getDescription());
            product.setCurrencyType(req.getCurrencyType());
            product.setProductType(req.getProductType());
            product.setTags(req.getTags() != null ? new ArrayList<>(req.getTags()) : new ArrayList<>());
            product.setAttributes(req.getAttributeDetails() != null ? new ArrayList<>(req.getAttributeDetails()) : new ArrayList<>());

            // Kategori
            Category category = categoryRepository.findById(UUID.fromString(req.getCategoryId()))
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // Alt kategoriler
            Set<SubCategory> subCategories = req.getSubCategories() != null
                    ? req.getSubCategories().stream()
                    .map(id -> subCategoryRepository.findById(UUID.fromString(id))
                            .orElseThrow(() -> new RuntimeException("SubCategory not found: " + id)))
                    .collect(Collectors.toSet())
                    : new HashSet<>();
            product.setSubCategories(subCategories);

            // Silinecek varyantlar (orphanRemoval ile Hibernate halledecek)
            if (req.getDeletedVariants() != null && !req.getDeletedVariants().isEmpty()) {
                for (String variantId : req.getDeletedVariants()) {
                    UUID varUUID = UUID.fromString(variantId);
                    variationRepository.findById(varUUID).ifPresent(var -> {
                        product.getVariations().remove(var);
                    });
                }
            }

            // Yeni / Güncellenmiş varyantlar
            List<Variation> updatedVariations = new ArrayList<>();
            if (req.getVariants() != null) {
                for (VariationUpdateRequest varReq : req.getVariants()) {
                    Variation var;
                    if (varReq.getId() != null && !varReq.getId().isEmpty()) {
                        var = variationRepository.findById(UUID.fromString(varReq.getId()))
                                .orElseThrow(() -> new NotFoundException("Variation not found: " + varReq.getId()));
                    } else {
                        var = new Variation();
                    }

                    // Varyant temel bilgileri
                    var.setProduct(product);
                    var.setModelName(varReq.getModelName());
                    var.setModelCode(varReq.getModelCode());
                    var.setColor(varReq.getColor());

                    // Attribute'lar - Her zaman yeni, değiştirilebilir bir liste oluşturup atayın
                    List<Attribute> newAttributes = varReq.getAttributes() != null
                            ? varReq.getAttributes().stream()
                            .map(attr -> new Attribute(
                                    attr.getPrice(),
                                    attr.getDiscountPrice(),
                                    attr.getStock(),
                                    attr.getMaxPurchaseStock(),
                                    attr.getSku(),
                                    attr.getBarcode(),
                                    attr.getAttributeDetails(),
                                    var
                            ))
                            .collect(Collectors.toList())
                            : new ArrayList<>();
                    var.setAttributes(newAttributes);

                    // Görseller - Burada da yeni bir liste oluşturup atıyoruz
                    Set<String> combinedImages = new LinkedHashSet<>();
                    if (var.getImages() != null) combinedImages.addAll(var.getImages());
                    if (varReq.getImageUrls() != null) combinedImages.addAll(varReq.getImageUrls());

                    if (images != null && !images.isEmpty()) {
                        for (MultipartFile image : images) {
                            Map<String, String> parsed = parseImageFileName(image.getOriginalFilename());
                            if (parsed == null) continue;

                            String imageModelCode = parsed.get("modelCode");
                            String imageColor = parsed.get("color");

                            if (varReq.getModelCode().equalsIgnoreCase(imageModelCode)
                                    && varReq.getColor().contains(imageColor)) {

                                String imageUrl = fileService.productFileUpload(
                                        image,
                                        product.getSeller().getSlug(),
                                        SlugGenerator.generateSlug(req.getName()),
                                        imageColor
                                );
                                combinedImages.add(imageUrl);
                            }
                        }
                    }
                    var.setImages(new ArrayList<>(combinedImages));

                    updatedVariations.add(var);
                }
            }

            // Varyant listesini güncelle
            // Burada da aynı mantığı izleyerek yeni bir ArrayList oluşturup atıyoruz.
            List<Variation> existingVariations = product.getVariations();
            existingVariations.clear();
            existingVariations.addAll(updatedVariations);

            // Silinecek görseller
            if (req.getDeleteImages() != null && !req.getDeleteImages().isEmpty()) {
                for (String imageUrlToDelete : req.getDeleteImages()) {
                    fileService.deleteFileProduct(imageUrlToDelete);
                    for (Variation var : product.getVariations()) {
                        if (var.getImages() != null) {
                            // Mevcut listeyi değiştirilebilir bir kopyaya dönüştürerek işlemi yapıyoruz
                            List<String> mutableImages = new ArrayList<>(var.getImages());
                            if(mutableImages.removeIf(img -> img.equals(imageUrlToDelete))) {
                                var.setImages(mutableImages);
                            }
                        }
                    }
                }
            }

            // Video işlemleri
            if (req.getVideoUrl() != null) {
                if (req.getVideoUrl().startsWith("temp")) {
                    String newPath = req.getVideoUrl().replace(
                            "temp/",
                            "products/" + product.getSeller().getSlug() + "/"
                    );
                    fileService.copyObject(req.getVideoUrl(), newPath);
                    product.setVideoUrl(newPath);
                    fileService.deleteInFolderFile(req.getVideoUrl());
                } else {
                    product.setVideoUrl(req.getVideoUrl());
                }
            } else {
                product.setVideoUrl(null);
            }

            productRepository.save(product);
            return new ApiResponse<>("Product Updated", HttpStatus.OK.value());

        } catch (Exception e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public ProductListDto getProductById(String id) {
        return productRepository.findById(UUID.fromString(id)).map(ProductListDto::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    private static Map<String, String> parseImageFileName(String filename) {
        String name = filename.contains(".") ? filename.substring(0, filename.lastIndexOf('.'))
                : filename;
        String[] parts = name.split("_");
        if (parts.length != 2) return null;

        Map<String, String> result = new HashMap<>();
        result.put("modelCode", parts[0]);
        result.put("color", parts[1]);
        return result;
    }

    public ProductDto getCustomerProduct(String id) {
        return productRepository.findById(UUID.fromString(id))
                .map(ProductDto::toDto)
                .orElseThrow(()-> new NotFoundException("Product not found"));
    }

    public ApiResponse<?> deleteProduct(String id) {
        Product product = productRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Product not found: " + id)
        );
        try {
            for (Variation var : product.getVariations()) {
                var.getImages().forEach(fileService::deleteFileProduct);
            }
            productRepository.delete(product);
            return new ApiResponse<>("Product Deleted", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    public ProductDto findBySlug(String slug)  {
        return productRepository.findBySlug(slug)
                .map(ProductDto::toDto)
                .orElseThrow(()-> new NotFoundException("Product not found"));
    }

    public Page<ProductListDto> findCompanyReturnProducts(String companyId, Pageable pageable) {
        return productRepository.findActiveProductsByCompanyId(UUID.fromString(companyId), pageable)
                .map(ProductListDto::toDto);
    }

    public Page<ProductDto> findProductBySeller(Pageable pageable) {
        Seller seller = sellerService.getSellerByUserId(authenticationFacade.getCurrentUserId());
        return productRepository.findActiveProductsByCompanyId(seller.getId(),pageable)
                .map(ProductDto::toDto);
    }

    public ProductDto findCompanyReturnProduct(String companyId, String slug) {
        return ProductDto.Companion.toDto(
                productRepository.findActiveProductByCompanyIdAndSlug(UUID.fromString(companyId), slug)
                        .orElseThrow(() -> new NotFoundException("Product not found: " + slug))
        );
    }

    public ProductDto getProductBySeller(String productId) {
        Seller seller = sellerService.getSellerByUserId(authenticationFacade.getCurrentUserId());
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(()-> new NotFoundException("Product not found: " + productId));
        if(Objects.equals(product.getSeller().getId(), seller.getId())) return ProductDto.Companion.toDto(product);
        throw new UnauthorizedException("Yetkisiz erişim size ait olmayan bir ürün çağırıyorsunuz !!");
    }

    public Page<ProductUiDto> findCompanyPopularOrNewSeasonProducts(String  companyId, String tag, Pageable pageable) {
        return productRepository.findPopularOrNewSeasonProducts(UUID.fromString(companyId), tag, pageable)
                .map(ProductUiDto::toProductUiDto);
    }

    public Page<ProductUiDto> getAllLProduct(Pageable pageable) {
        return productRepository.findActiveProducts(pageable).map(ProductUiDto::toProductUiDto);
    }

    public Page<ProductDto> findAllAdminProduct(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDto::toDto);
    }

    public Page<ProductUiDto> filterProduct(String color,String clothSize, List<String> tags,String style,
                                              String subCategoryName,
                                              String categoryName,
                                              String searchParam,
                                              Pageable pageable) {

        color = (color == null || color.isEmpty() ? null : color);
        clothSize = (clothSize == null || clothSize.isEmpty() ? null : clothSize);
        tags = (tags == null || tags.isEmpty() ? null : tags);
        style = (style == null || style.isEmpty() ? null : style);
        subCategoryName = (subCategoryName == null || subCategoryName.isEmpty() ? null : subCategoryName);
        categoryName = (categoryName == null || categoryName.isEmpty() ? null : categoryName);
        searchParam = (searchParam == null || searchParam.isEmpty() ? null : searchParam);

        return productRepository.findByQueryField(color, clothSize, tags, style,subCategoryName,categoryName,searchParam,pageable)
                .map(ProductUiDto::toProductUiDto);
    }

    public Page<ProductDto> filterAdminProduct(String color,String clothSize, List<String> tags,String style,
                                               String subCategoryName,
                                               String categoryName,
                                               String searchParam,
                                               Pageable pageable) {

        color = (color == null || color.isEmpty() ? null : color);
        clothSize = (clothSize == null || clothSize.isEmpty() ? null : clothSize);
        tags = (tags == null || tags.isEmpty() ? null : tags);
        style = (style == null || style.isEmpty() ? null : style);
        subCategoryName = (subCategoryName == null || subCategoryName.isEmpty() ? null : subCategoryName);
        categoryName = (categoryName == null || categoryName.isEmpty() ? null : categoryName);
        searchParam = (searchParam == null || searchParam.isEmpty() ? null : searchParam);

        return productRepository.findByQueryField(color, clothSize, tags, style,subCategoryName,categoryName,searchParam,pageable)
                .map(ProductDto::toDto);
    }

    public ApiResponse<?> changeProductActiveStatus(String productId, Boolean active) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        try {
            product.setActive(active);
            Product savedProduct =  productRepository.save(product);

            if(active) {
                SearchItem searchItem = new SearchItem();
                searchItem.setId(savedProduct.getId().toString());
                searchItem.setName(savedProduct.getName());
                searchItem.setItemType(SearchItemType.PRODUCT);
                searchItemService.createIndex(searchItem);
            } else {
                searchItemService.deleteItem(savedProduct.getId().toString());
            }

            return new ApiResponse<>("Product Status Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public Cart toCartItem(List<AddToCartRequest> cartItems, String userId) {

        List<CartItem> cartItemList = new ArrayList<>();

        cartItems.forEach(cart -> {

            Product product = productRepository.findById(UUID.fromString(cart.getProductId()))
                    .orElseThrow(() -> new NotFoundException("Product not found: " + cart.getProductId()));

            Variation variation = product.getVariations().stream()
                    .filter(v -> v.getId().equals(UUID.fromString(cart.getVariationId())))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Variation not found"));

            Attribute attribute = variation.getAttributes().stream()
                    .filter(attr -> attr.getId().equals(UUID.fromString(cart.getAttributeId())))
                    .findFirst()
                    .orElse(null);

            if(cart.getQuantity() > attribute.getStock()) {
                throw new StockException("Quantity exceeded");
            }

            CartItem cartItem = new CartItem();
            cartItem.setAttributeId(attribute != null ? attribute.getId().toString() : null);
            cartItem.setVariationId(variation.getId().toString());
            cartItem.setColor(variation.getColor());
            cartItem.setModelCode(variation.getModelCode());
            cartItem.setProductSlug(product.getSlug());
            cartItem.setProductId(product.getId().toString());
            cartItem.setMaxPurchaseStock(attribute.getMaxPurchaseStock());
            cartItem.setName(product.getName());
            cartItem.setQuantity(cart.getQuantity());
            cartItem.setPrice(attribute.getPrice());
            cartItem.setBrandName(product.getBrandName());
            cartItem.setImage(!variation.getImages().isEmpty() ? variation.getImages().get(0) : null);

            cartItem.setAttributes(
                    attribute != null ? attribute.getAttributeDetails().stream()
                            .map(at -> new CartAttributes(at.getKey(), at.getValue()))
                            .collect(Collectors.toList()) : Collections.emptyList()
            );

            cartItemList.add(cartItem); // 🔹 Eksik olan kısım
        });

        BigDecimal totalPrice = cartItemList.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Cart cart = new Cart();
        cart.setId(userId);
        cart.setCartItems(cartItemList);
        cart.setTotalPrice(totalPrice);
        cart.setItemCount(cartItemList.stream().mapToInt(CartItem::getQuantity).sum());

        return cart;
    }

    public Page<ProductUiDto> getProductsByCategory(String categoryName, Pageable pageable) {
        return productRepository.findProductByCategoryOrSubCategory(categoryName, pageable)
                .map(ProductUiDto::toProductUiDto);
    }

}
