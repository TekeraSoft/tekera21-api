package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.request.VariationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.*;
import com.tekerasoft.tekeramarketplace.repository.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.ProductRepository;
import com.tekerasoft.tekeramarketplace.repository.SubCategoryRepository;
import io.minio.errors.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public ProductService(ProductRepository productRepository,
                          FileService fileService,
                          CategoryRepository categoryRepository,
                          SubCategoryRepository subCategoryRepository) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @Transactional
    public ApiResponse<?> create(CreateProductRequest req, List<MultipartFile> images) {
        try {
            Product product = new Product();
            product.setName(req.getName());
            product.setSlug(req.getSlug());
            product.setCode(req.getCode());
            product.setDescription(req.getDescription());
            product.setCurrencyType(req.getCurrencyType());
            product.setProductType(req.getProductType());
            product.setTags(req.getTags());
            product.setAttributes(req.getAttributes());

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

            Map<String, Map<String, List<String>>> imageMap = groupImagesByModelAndColor(images);

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

                // Renk bilgisini attribute'lardan çek
                String color = varReq.getAttributes().stream()
                        .filter(attr -> attr.getName().equalsIgnoreCase("color"))
                        .map(Attribute::getValue)
                        .findFirst()
                        .orElse(null);

                List<String> matchedImages = Optional.ofNullable(
                        imageMap.getOrDefault(slugify(varReq.getModelName()), new HashMap<>())
                                .getOrDefault(slugify(color), new ArrayList<>())
                ).orElse(new ArrayList<>());

                var.setImages(matchedImages);
                variations.add(var);
            }
            productRepository.save(product);
            return new ApiResponse<>("Product Created", null, true);
        } catch (RuntimeException | ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    private Map<String, Map<String, List<String>>> groupImagesByModelAndColor(List<MultipartFile> images) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Map<String, Map<String, List<String>>> imageMap = new HashMap<>();
        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            if (fileName == null || !fileName.contains("-") || !fileName.contains("_")) continue;

            String[] nameParts = fileName.split("_")[0].split("-");
            if (nameParts.length != 2) continue;

            String model = nameParts[0];
            String color = nameParts[1];

            String uploadedName = fileService.productFileUpload(image, model + "-" + color);

            imageMap
                    .computeIfAbsent(model, k -> new HashMap<>())
                    .computeIfAbsent(color, k -> new ArrayList<>())
                    .add(uploadedName);
        }
        return imageMap;
    }

    private String slugify(String text) {
        return text == null ? "" : text.toLowerCase().replace(" ", "").replaceAll("[^a-z0-9]", "");
    }

}
