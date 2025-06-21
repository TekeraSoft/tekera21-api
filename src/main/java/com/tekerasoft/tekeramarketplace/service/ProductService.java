package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItemType;
import com.tekerasoft.tekeramarketplace.repository.jparepository.*;
import com.tekerasoft.tekeramarketplace.specification.ProductSpecification;
import com.tekerasoft.tekeramarketplace.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CompanyRepository companyRepository;
    private final SearchItemService searchItemService;
    private final VariationRepository variationRepository;
    private final AttributeRepository attributeRepository;

    public ProductService(ProductRepository productRepository,
                          FileService fileService,
                          CategoryRepository categoryRepository,
                          SubCategoryRepository subCategoryRepository,
                          CompanyRepository companyRepository, SearchItemService searchItemService, VariationRepository variationRepository, AttributeRepository attributeRepository) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.companyRepository = companyRepository;
        this.searchItemService = searchItemService;
        this.variationRepository = variationRepository;
        this.attributeRepository = attributeRepository;
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
            product.setAttributes(req.getAttributes());
            product.setActive(true);

            // Company
            Company company = companyRepository.findById(UUID.fromString(req.getCompanyId())).orElseThrow();
            product.setCompany(company);

            // Category
            Category category = categoryRepository.findById(UUID.fromString(req.getCategoryId()))
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // SubCategory
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

                // Variation attributes
                List<Attribute> variationAttributes = varReq.getAttributes().stream()
                        .map(attr -> new Attribute(
                                attr.getStockAttribute(),
                                attr.getStock(),
                                attr.getPrice(),
                                attr.getDiscountPrice(),
                                attr.getSku(),
                                attr.getBarcode(),
                                var
                        )).collect(Collectors.toList());
                var.setAttributes(variationAttributes);

                List<String> imgUrls = new ArrayList<>();

                Set<String> variantColors = varReq.getAttributes().stream()
                        .flatMap(attr -> attr.getStockAttribute().stream())
                        .filter(attr -> attr.getKey().equalsIgnoreCase("color") || attr.getKey().equalsIgnoreCase("renk"))
                        .map(StockAttribute::getValue)
                        .collect(Collectors.toSet());

                for (MultipartFile image : images) {
                    Map<String, String> parsed = parseImageFileName(image.getOriginalFilename());
                    if (parsed == null) continue;

                    String imageModelCode = parsed.get("modelCode");
                    String imageColor = parsed.get("color");

                    if (varReq.getModelCode().equalsIgnoreCase(imageModelCode)
                            && variantColors.contains(imageColor)) {

                        String imageUrl = fileService.productFileUpload(
                                image,
                                company.getName(),
                                SlugGenerator.generateSlug(req.getName()),
                                imageColor
                        );
                        imgUrls.add(imageUrl);
                    }
                }

                var.setImages(imgUrls);
                variations.add(var);
            }
            product.setVariations(variations);
            productRepository.save(product);

            return new ApiResponse<>("Product Created", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    @Transactional
    public ApiResponse<?> update(UpdateProductRequest req, List<MultipartFile> images) {
        try {
            Product product = productRepository.findById(UUID.fromString(req.getId()))
                    .orElseThrow(() -> new NotFoundException("Product not found: " + req.getId()));
            product.setName(req.getName());
            product.setSlug(SlugGenerator.generateSlug(req.getName()));
            product.setCode(req.getCode());
            product.setBrandName(req.getBrandName());
            product.setDescription(req.getDescription());
            product.setCurrencyType(req.getCurrencyType());
            product.setProductType(req.getProductType());
            product.setTags(req.getTags());
            product.setAttributes(req.getAttributes());
            product.setActive(true);

            // Category
            Category category = categoryRepository.findById(UUID.fromString(req.getCategoryId()))
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            // SubCategory
            Set<SubCategory> subCategories = req.getSubCategories().stream()
                    .map(id -> subCategoryRepository.findById(UUID.fromString(id))
                            .orElseThrow(() -> new RuntimeException("SubCategory not found: " + id)))
                    .collect(Collectors.toSet());
            product.setSubCategories(subCategories);

            // Variations
            List<Variation> variations = new ArrayList<>();
            for (VariationUpdateRequest varReq : req.getVariants()) {
                Variation var = variationRepository.findById(UUID.fromString(varReq.getId()))
                        .orElseThrow(() -> new NotFoundException("Variation not found: " + varReq.getId()));
                var.setModelName(varReq.getModelName());
                var.setModelCode(varReq.getModelCode());
                var.setProduct(product);

                var.getAttributes().clear();

                // Variation attributes
                // 🔁 SONRA YENİLERİNİ EKLE
                List<Attribute> variationAttributes = varReq.getAttributes().stream()
                        .map(attr -> new Attribute(
                                attr.getStockAttribute(),
                                attr.getStock(),
                                attr.getPrice(),
                                attr.getDiscountPrice(),
                                attr.getSku(),
                                attr.getBarcode(),
                                var
                        )).collect(Collectors.toList());

                var.getAttributes().addAll(variationAttributes);

                if(!images.isEmpty()) {
                    List<String> imgUrls = new ArrayList<>(var.getImages());

                    Set<String> variantColors = varReq.getAttributes().stream()
                            .flatMap(attr -> attr.getStockAttribute().stream())
                            .filter(attr -> attr.getKey().equalsIgnoreCase("color") || attr.getKey().equalsIgnoreCase("renk"))
                            .map(StockAttribute::getValue)
                            .collect(Collectors.toSet());

                    for (MultipartFile image : images) {
                        Map<String, String> parsed = parseImageFileName(image.getOriginalFilename());
                        if (parsed == null) continue;

                        String imageModelCode = parsed.get("modelCode");
                        String imageColor = parsed.get("color");

                        if (varReq.getModelCode().equalsIgnoreCase(imageModelCode)
                                && variantColors.contains(imageColor)) {

                            String imageUrl = fileService.productFileUpload(
                                    image,
                                    product.getCompany().getName(),
                                    SlugGenerator.generateSlug(req.getName()),
                                    imageColor
                            );
                            imgUrls.add(imageUrl);
                        }
                    }
                    var.setImages(imgUrls);
                }
                variations.add(var);
            }

            if (req.getDeleteImages() != null && !req.getDeleteImages().isEmpty()) {
                for (String imageUrlToDelete : req.getDeleteImages()) {
                    // 1. Fiziksel dosyayı MinIO'dan sil
                    fileService.deleteFileProduct(imageUrlToDelete);

                    // 2. Tüm varyasyonları gezip, bu URL varsa listesinden çıkar
                    for (Variation var : variations) {
                        List<String> updatedImages = new ArrayList<>(var.getImages());
                        if (updatedImages.removeIf(img -> img.equals(imageUrlToDelete))) {
                            var.setImages(updatedImages); // Listeyi yeniden set et
                        }
                    }
                }
            }

            product.setVariations(variations);
            productRepository.save(product);

            return new ApiResponse<>("Product Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    private static String getColorFromAttributes(List<StockAttribute> attributes) {
        for (StockAttribute attr : attributes) {
            String key = attr.getKey().toLowerCase();
            if (key.contains("color") || key.contains("renk")) {
                return attr.getValue();
            }
        }
        return "";
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
            searchItemService.deleteItem(product.getId().toString());
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

    public Page<ProductListDto> findAllListProduct(Pageable pageable) {
        return productRepository.findActiveProducts(pageable).map(ProductListDto::toDto);
    }

    public Page<ProductListDto> filterProducts(FilterProductRequest req, Pageable pageable) {
        Specification<Product> spec = ProductSpecification
                .hasVariationAttributesWithOptionalModelName(req.getModelName(), req.getAttributes());
        return productRepository.findAll(spec, pageable).map(ProductListDto::toDto);
    }

    public ApiResponse<?> changeProductActiveStatus(String productId, Boolean active) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        try {
            product.setActive(active);
            Product saveProduct = productRepository.save(product);

            if(active) {
                SearchItem searchItem = new SearchItem();
                searchItem.setId(saveProduct.getId().toString());
                searchItem.setName(saveProduct.getName());
                searchItem.setItemType(SearchItemType.PRODUCT);
                searchItemService.createIndex(searchItem);
            } else {
                searchItemService.deleteItem(saveProduct.getId().toString());
            }

            return new ApiResponse<>("Product Status Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

}
