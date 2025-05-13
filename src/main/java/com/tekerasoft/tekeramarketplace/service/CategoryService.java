package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.CategoryDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.Category;
import com.tekerasoft.tekeramarketplace.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public CategoryService(CategoryRepository categoryRepository, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    public ApiResponse<?> save(CreateCategoryRequest req)
    {
        try {
            Category category = new Category();
            String imagePath = fileService.folderFileUpload(req.getFile(),"category");
            category.setName(req.getName());
            category.setImage(imagePath);
            category.setSubCategories(new ArrayList<>());
            categoryRepository.save(category);
            return new ApiResponse<>("Category Created", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error saving category", e);
        }
    }

    public List<CategoryDto> getAllCategory() {
        return categoryRepository.findAll().stream().map(CategoryDto::toDto).toList();
    }
}
