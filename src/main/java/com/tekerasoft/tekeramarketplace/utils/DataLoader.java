package com.tekerasoft.tekeramarketplace.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekerasoft.tekeramarketplace.dto.CategoryDto;
import com.tekerasoft.tekeramarketplace.dto.CategoryLoaderDto;
import com.tekerasoft.tekeramarketplace.dto.SubCategoryDto;
import com.tekerasoft.tekeramarketplace.dto.SubCategoryLoader;
import com.tekerasoft.tekeramarketplace.dto.request.VariationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ColorSize;
import com.tekerasoft.tekeramarketplace.dto.response.OldProduct;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CompanyRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ProductRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SubCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    // Constructorı güncelledik
    public DataLoader(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
                      ObjectMapper objectMapper,
                      ProductRepository productRepository,
                      CompanyRepository companyRepository) { // <<-- 2. DEĞİŞİKLİK: Bu parametreyi ekledik
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository; // <<-- Bu satırı ekledik (2. değişikliğin parçası)
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

/*      InputStream inputStream = getClass().getClassLoader().getResourceAsStream("arzuamber-products.json");
      System.out.println("Veriler yükleniyor...");
        List<OldProduct> oldProducts = objectMapper.readValue(inputStream, new TypeReference<List<OldProduct>>() {});
        for (OldProduct old: oldProducts) {
            Product product = new Product();
            product.setName(old.getName());
            product.setSlug(old.getSlug());
            product.setDescription(old.getDescription());
            product.setCurrencyType(CurrencyType.TRY);
            product.setProductType(ProductType.PHYSICAL);
            product.setActive(true);

            Company company = companyRepository.findById(UUID.fromString("452531d6-343c-4952-a923-af51f3bffe2b")).orElseThrow();
            product.setCompany(company);

            Category category = categoryRepository.findById(UUID.fromString("44f087b3-ad70-4782-bed0-8ec39c56d7c1"))
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            List<Variation> variations = new ArrayList<>();
            for (ColorSize varReq : old.getColorSize()) {
                Variation var = new Variation();
                var.setModelName(varReq.getColor()+"-"+varReq.getStockCode());
                var.setModelCode(varReq.getStockCode());
                var.setProduct(product);
                var.setColor(varReq.getColor());

                // Variation attributes
                List<Attribute> variationAttributes = varReq.getStockSize().stream()
                        .map(attr -> {
                            List<AttributeDetail> attributeDetails = new ArrayList<>();
                            attributeDetails.add(new AttributeDetail("size",attr.getSize()));
                           return new Attribute(
                                    old.getPrice(),
                                    old.getDiscountPrice(),
                                    attr.getStock(),
                                    varReq.getStockCode(),
                                    null,
                                    attributeDetails,
                                    var
                            );
                        }).collect(Collectors.toList());
                var.setAttributes(variationAttributes);
                var.setImages(varReq.getImages().stream().map((i) -> "/products/arzuamber_moda/" + i).toList());
                variations.add(var);
            }
            product.setVariations(variations);
            productRepository.save(product);
        }
        System.out.println("Veriler sorunsuz yüklendi :)");*/
    }
}

/* System.out.println("JSON verileri veritabanına yükleniyor...");
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("categories.json");
            if (inputStream == null) {
                System.out.println("data.json dosyası bulunamadı! Lütfen 'src/main/resources' altına eklediğinizden emin olun.");
                return;
            }

            // Mevcut verileri temizlemek isteyebilirsiniz (isteğe bağlı ve dikkatli kullanılmalı)
            // subCategoryRepository.deleteAll();
            // categoryRepository.deleteAll();

            // JSON'ı sizin DTO'larınıza oku
            List<CategoryLoaderDto> categoriesDto = objectMapper.readValue(inputStream, new TypeReference<List<CategoryLoaderDto>>() {});

            for (CategoryLoaderDto categoryLoaderDto : categoriesDto) {
                // CategoryLoaderDto'dan Category entity'ye mapleme
                // Kotlin'deki null safety Java'da doğrudan olmadığından,
                // Objects.requireNonNull kullanarak null kontrolü yapıyoruz.
                Category category = new Category();
                category.setName(categoryLoaderDto.getName());
                category.setSlug(categoryLoaderDto.getSlug());
                category.setImage(null);
                categoryRepository.save(category);

                if (categoryLoaderDto.getSubCategories() != null) {
                    for (SubCategoryLoader subCategoryLoader : categoryLoaderDto.getSubCategories()) {
                        // SubCategoryLoader'dan SubCategory entity'ye mapleme ve kaydetme
                        saveSubCategory(subCategoryLoader, category, null);
                    }
                }
            }
            System.out.println("Veriler başarıyla yüklendi!");
        } catch (Exception e) {
            System.out.println("Veri yüklenirken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveSubCategory(
            SubCategoryLoader subCategoryLoader,
            Category category,
            SubCategory parentSubCategory
    ) {
        // SubCategoryLoader'dan SubCategory entity'ye mapleme
        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryLoader.getName());
        subCategory.setSlug(subCategoryLoader.getSlug());
        subCategory.setImage(null);
        subCategory.setCategory(category);
        subCategory.setParent(parentSubCategory);
        subCategoryRepository.save(subCategory);

        // Parent-child ilişkisini yönetme
        // Kotlin'deki mutableListOf() Java'da add metodunu destekler.
        // Parent'ın children listesi null değilse ekle.
        if (parentSubCategory != null && parentSubCategory.getChildren() != null) {
            parentSubCategory.getChildren().add(subCategory);
        }

        // Çocuk alt kategorileri recursive olarak kaydetme
        if (subCategoryLoader.getChildren() != null) {
            for (SubCategoryLoader childLoader : subCategoryLoader.getChildren()) {
                saveSubCategory(childLoader, category, subCategory);
            }
        }
    }*/