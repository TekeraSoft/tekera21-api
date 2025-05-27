package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.CreateSubCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.Category;
import com.tekerasoft.tekeramarketplace.model.entity.SubCategory;
import com.tekerasoft.tekeramarketplace.repository.releational.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.releational.SubCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public SubCategoryService(SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository, FileService fileService) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    public ApiResponse createSubCategory(CreateSubCategoryRequest req) {
        try {
            Category category = categoryRepository.findById(UUID.fromString(req.getCategoryId()))
                    .orElseThrow();
            String imagePath = fileService.folderFileUpload(req.getImage(),"sub-category");
            SubCategory subCategory = new SubCategory();
            subCategory.setCategory(category);
            subCategory.setName(req.getName());
            subCategory.setImage(imagePath);
            subCategoryRepository.save(subCategory);
            return new ApiResponse("Sub Categories Created", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
