package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.CreateDigitalFashionCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.DigitalFashionCategoryException;
import com.tekerasoft.tekeramarketplace.model.entity.DigitalFashionCategory;
import com.tekerasoft.tekeramarketplace.repository.jparepository.DigitalFashionCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DigitalFashionCategoryService {
    private final DigitalFashionCategoryRepository digitalFashionCategoryRepository;
    private final FileService fileService;

    public DigitalFashionCategoryService(DigitalFashionCategoryRepository digitalFashionCategoryRepository,
                                         FileService fileService) {
        this.digitalFashionCategoryRepository = digitalFashionCategoryRepository;
        this.fileService = fileService;
    }

    public ApiResponse<?> createDigitalFashionCategory(CreateDigitalFashionCategoryRequest req) {
        if(digitalFashionCategoryRepository.existsByName(req.getName())) {
            throw new DigitalFashionCategoryException("Category already exists");
        }
        try {
            DigitalFashionCategory cat = new DigitalFashionCategory();
            String imagePath = fileService.folderFileUpload(req.getFile(), "df_category");
            cat.setName(req.getName());
            cat.setImage(imagePath);
            digitalFashionCategoryRepository.save(cat);
            return new  ApiResponse<>("Created DigitalFashionCategory", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new  DigitalFashionCategoryException(e.getMessage());
        }
    }
}
