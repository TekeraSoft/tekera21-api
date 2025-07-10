package com.tekerasoft.tekeramarketplace.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekerasoft.tekeramarketplace.dto.CategoryLoaderDto;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository; // <<-- 1. DEĞİŞİKLİK: Bu satırı ekledik
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    // Constructorı güncelledik
    public DataLoader(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, ObjectMapper objectMapper, ProductRepository productRepository, CompanyRepository companyRepository) { // <<-- 2. DEĞİŞİKLİK: Bu parametreyi ekledik
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository; // <<-- Bu satırı ekledik (2. değişikliğin parçası)
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() != 0) return;

        InputStream jsonStream =
                new ClassPathResource("/static/categories.json").getInputStream();

        List<CategoryLoaderDto> loaderDtoList =
                objectMapper.readValue(jsonStream,
                        new TypeReference<List<CategoryLoaderDto>>() {});

        loaderDtoList.forEach(dto -> {
            // 1) Kategori nesnesini oluştur
            Category category = new Category();
            category.setName(dto.getKategori());
            category.setSlug(SlugGenerator.generateSlug(dto.getKategori()));

            // 2) Gelen listeyi dön, SubCategory nesnelerini üret
            List<SubCategory> subs = dto.getAlt_kategoriler() == null
                    ? Collections.emptyList()
                    : dto.getAlt_kategoriler().stream()
                    .map(name -> {
                        SubCategory s = new SubCategory();
                        s.setName(name);
                        s.setSlug(SlugGenerator.generateSlug(name));
                        s.setCategory(category);          // ilişkiyi kur
                        return s;
                    })
                    .collect(Collectors.toList());

            // 3) Hazırladığın listeyi kategorinin içine yerleştir
            category.setSubCategories(new ArrayList<>(subs));

            // 4) Tek seferde kaydet – cascade ALL alt kategorileri de insert eder
            categoryRepository.save(category);
        });

        /*InputStream inputStream = getClass().getClassLoader().getResourceAsStream("arzuamber-products.json");
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
        }
    }*/
}

