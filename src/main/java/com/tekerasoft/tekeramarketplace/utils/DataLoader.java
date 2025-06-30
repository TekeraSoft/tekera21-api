package com.tekerasoft.tekeramarketplace.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekerasoft.tekeramarketplace.dto.CategoryLoaderDto;
import com.tekerasoft.tekeramarketplace.model.entity.Category;
import com.tekerasoft.tekeramarketplace.model.entity.SubCategory;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SubCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Eğer projenizde yoksa SubCategoryRepository'yi import etmeniz gerekecek.
// import com.yourpackage.repository.SubCategoryRepository;


@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository; // <<-- 1. DEĞİŞİKLİK: Bu satırı ekledik
    private final ObjectMapper objectMapper;

    // Constructor'ı güncelledik
    public DataLoader(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, ObjectMapper objectMapper) { // <<-- 2. DEĞİŞİKLİK: Bu parametreyi ekledik
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository; // <<-- Bu satırı ekledik (2. değişikliğin parçası)
        this.objectMapper = objectMapper;
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
    }
}

