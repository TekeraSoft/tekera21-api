package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.request.VariationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.repository.releational.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.releational.CompanyRepository;
import com.tekerasoft.tekeramarketplace.repository.releational.ProductRepository;
import com.tekerasoft.tekeramarketplace.repository.releational.SubCategoryRepository;
import com.tekerasoft.tekeramarketplace.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ProductService(ProductRepository productRepository,
                          FileService fileService,
                          CategoryRepository categoryRepository,
                          SubCategoryRepository subCategoryRepository,
                          CompanyRepository companyRepository) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.companyRepository = companyRepository;
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
                var.setPrice(varReq.getPrice());
                var.setStock(varReq.getStock());
                var.setSku(varReq.getSku());
                var.setBarcode(varReq.getBarcode());
                var.setAttributes(varReq.getAttributes());
                var.setProduct(product);

                List<String> imgUrls = new ArrayList<>();

                for (MultipartFile image : images) {
                    Map<String, String> parsed = parseImageFileName(image.getOriginalFilename());
                    if (parsed == null) continue;

                    String imageModelCode = parsed.get("modelCode");
                    String imageColor = parsed.get("color");
                    String variantColor = getColorFromAttributes(varReq.getAttributes());
                    if (varReq.getModelCode().equalsIgnoreCase(imageModelCode)
                            && variantColor.trim().equalsIgnoreCase(imageColor.trim())) {

                        String imageUrl = fileService.productFileUpload(image, company.getName(),
                                SlugGenerator.generateSlug(req.getName()));
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

    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepository.findActiveProducts(pageable).map(ProductDto::toDto);
    }

    public ApiResponse<?> changeProductActiveStatus(String productId, Boolean active) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        try {
            product.setActive(active);
            productRepository.save(product);
            return new ApiResponse<>("Product Status Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    private static String getColorFromAttributes(List<Attribute> attributes) {
        for (Attribute attr : attributes) {
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

}
