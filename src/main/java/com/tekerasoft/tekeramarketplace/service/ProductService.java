package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.ProductUiDto;
import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.repository.jparepository.*;
import com.tekerasoft.tekeramarketplace.utils.ResizeProductVideo;
import com.tekerasoft.tekeramarketplace.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CompanyRepository companyRepository;
    private final VariationRepository variationRepository;
    private final ResizeProductVideo resizeProductVideo;

    public ProductService(ProductRepository productRepository,
                          FileService fileService,
                          CategoryRepository categoryRepository,
                          SubCategoryRepository subCategoryRepository,
                          CompanyRepository companyRepository,
                          VariationRepository variationRepository,
                          ResizeProductVideo resizeProductVideo) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.companyRepository = companyRepository;
        this.variationRepository = variationRepository;
        this.resizeProductVideo = resizeProductVideo;
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
            Company company = companyRepository.findById(UUID.fromString(req.getCompanyId()))
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            product.setCompany(company);

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
                                    company.getSlug(),
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
    public ApiResponse<?> update(UpdateProductRequest req ,List<MultipartFile> images) {
        try {
            Product product = productRepository.findById(UUID.fromString(req.getId()))
                    .orElseThrow(() -> new NotFoundException("Product not found: " + req.getId()));

            // ---------- Temel alanlar ----------
            product.setName(req.getName());
            product.setCode(req.getCode());
            product.setBrandName(req.getBrandName());
            product.setDescription(req.getDescription());
            product.setCurrencyType(req.getCurrencyType());
            product.setProductType(req.getProductType());
            product.setTags(req.getTags());
            product.setAttributes(req.getAttributeDetails());

            // ---------- Kategori ----------
            Category category = categoryRepository.findById(UUID.fromString(req.getCategoryId()))
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // ---------- Alt kategoriler ----------
            Set<SubCategory> subCategories = req.getSubCategories().stream()
                    .map(id -> subCategoryRepository.findById(UUID.fromString(id))
                            .orElseThrow(() -> new RuntimeException("SubCategory not found: " + id)))
                    .collect(Collectors.toSet());
            product.setSubCategories(subCategories);

            // ---------- Varyasyonlar (sırayı koruyarak) ----------
            List<Variation> orderedVariations = new ArrayList<>();

            for (VariationUpdateRequest varReq : req.getVariants()) {

                Variation var = variationRepository.findById(UUID.fromString(varReq.getId()))
                        .orElseThrow(() -> new NotFoundException("Variation not found: " + varReq.getId()));

                if (varReq.getId() != null && !varReq.getId().isEmpty()) {
                    var.getAttributes().clear(); // mevcut attributelar sıfırlanıyor
                } else {
                    var = new Variation();
                    var.setAttributes(new ArrayList<>());
                }

                // Varyasyon temel bilgileri
                var.setProduct(product);
                var.setModelName(varReq.getModelName());
                var.setModelCode(varReq.getModelCode());
                var.setColor(varReq.getColor());

                // Attribute'lar
                Variation finalVar = var;
                List<Attribute> variationAttributes = varReq.getAttributes().stream()
                        .map(attr -> new Attribute(
                                attr.getPrice(),
                                attr.getDiscountPrice(),
                                attr.getStock(),
                                attr.getSku(),
                                attr.getBarcode(),
                                attr.getAttributeDetails(),
                                finalVar
                        ))
                        .collect(Collectors.toList());
                var.getAttributes().addAll(variationAttributes);

                // Görselleri birleştir (gelen url'ler + yüklenen dosyalar)
                Set<String> combinedImages = new LinkedHashSet<>(); // sırayı korur, tekrarları engeller

                if (var.getImages() != null) {
                    combinedImages.addAll(var.getImages()); // mevcut görseller
                }

                if (varReq.getImageUrls() != null) {
                    combinedImages.addAll(varReq.getImageUrls()); // gelen görsel URL'leri
                }

                if (images != null && !images.isEmpty()) {
                    for (MultipartFile image : images) {
                        Map<String, String> parsed = parseImageFileName(image.getOriginalFilename());
                        if (parsed == null) continue;

                        String imageModelCode = parsed.get("modelCode");
                        String imageColor     = parsed.get("color");

                        if (varReq.getModelCode().equalsIgnoreCase(imageModelCode)
                                && varReq.getColor().contains(imageColor)) {

                            String imageUrl = fileService.productFileUpload(
                                    image,
                                    product.getCompany().getSlug(),
                                    SlugGenerator.generateSlug(req.getName()),
                                    imageColor
                            );
                            combinedImages.add(imageUrl);
                        }
                    }
                }

                var.setImages(new ArrayList<>(combinedImages));

                orderedVariations.add(var);
            }

            // Eski listeyi temizle + sıralı listeyi ekle
            product.getVariations().clear();
            product.getVariations().addAll(orderedVariations);

            // ---------- Silinecek görseller ----------
            if (req.getDeleteImages() != null && !req.getDeleteImages().isEmpty()) {
                for (String imageUrlToDelete : req.getDeleteImages()) {
                    fileService.deleteFileProduct(imageUrlToDelete);
                    for (Variation var : product.getVariations()) {
                        List<String> updatedImages = new ArrayList<>(var.getImages() == null ? List.of() : var.getImages());
                        if (updatedImages.removeIf(img -> img.equals(imageUrlToDelete))) {
                            var.setImages(updatedImages);
                        }
                    }
                }
            }
            if(req.getVideoUrl() != null) {
                if(req.getVideoUrl().startsWith("temp")) {
                    String newPath = req.getVideoUrl().replace("temp/", "products/"+product.getCompany().getSlug()+"/");
                    fileService.copyObject(req.getVideoUrl(), newPath);
                    product.setVideoUrl(newPath);
                    fileService.deleteInFolderFile(req.getVideoUrl());
                }else {
                    product.setVideoUrl(req.getVideoUrl());
                }
            } else {
                product.setVideoUrl(null);
            }
            productRepository.save(product);
            return new ApiResponse<>("Product Updated", HttpStatus.OK.value());

        } catch (RuntimeException e) {
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

    public ProductDto findCompanyReturnProduct(String companyId, String slug) {
        return ProductDto.Companion.toDto(
                productRepository.findActiveProductByCompanyIdAndSlug(UUID.fromString(companyId), slug)
                        .orElseThrow(() -> new NotFoundException("Product not found: " + slug))
        );
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
                                              Pageable pageable) {

        color = (color == null || color.isEmpty() ? null : color);
        clothSize = (clothSize == null || clothSize.isEmpty() ? null : clothSize);
        tags = (tags == null || tags.isEmpty() ? null : tags);
        style = (style == null || style.isEmpty() ? null : style);
        subCategoryName = (subCategoryName == null || subCategoryName.isEmpty() ? null : subCategoryName);

        return productRepository.findByQueryField(color, clothSize, tags, style,subCategoryName, pageable)
                .map(ProductUiDto::toProductUiDto);
    }

    public Page<ProductDto> filterAdminProduct(String color,String clothSize, List<String> tags,String style,
                                               String subCategoryName,
                                               Pageable pageable) {

        color = (color == null || color.isEmpty() ? null : color);
        clothSize = (clothSize == null || clothSize.isEmpty() ? null : clothSize);
        tags = (tags == null || tags.isEmpty() ? null : tags);
        style = (style == null || style.isEmpty() ? null : style);
        subCategoryName = (subCategoryName == null || subCategoryName.isEmpty() ? null : subCategoryName);

        return productRepository.findByQueryField(color, clothSize, tags, style,subCategoryName, pageable)
                .map(ProductDto::toDto);
    }

    public ApiResponse<?> changeProductActiveStatus(String productId, Boolean active) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        try {
            product.setActive(active);
            productRepository.save(product);

//            if(active) {
//                SearchItem searchItem = new SearchItem();
//                searchItem.setId(saveProduct.getId().toString());
//                searchItem.setName(saveProduct.getName());
//                searchItem.setItemType(SearchItemType.PRODUCT);
//                searchItemService.createIndex(searchItem);
//            } else {
//                searchItemService.deleteItem(saveProduct.getId().toString());
//            }

            return new ApiResponse<>("Product Status Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public Page<ProductUiDto> getProductsBySubCategory(String subName,Pageable pageable) {
        return productRepository.findProductBySubCategory(subName,pageable).map(ProductUiDto::toProductUiDto);
    }
}
