package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.CategoryDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.CategoryException;
import com.tekerasoft.tekeramarketplace.model.entity.Category;
import com.tekerasoft.tekeramarketplace.repository.releational.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public CategoryService(CategoryRepository categoryRepository, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    public ApiResponse<?> save(CreateCategoryRequest req) {
        if (categoryRepository.existsByName(req.getName())) {
            throw new CategoryException("Category name already exists");
        }
        String imagePath = fileService.folderFileUpload(req.getFile(), "category");
        Category category = new Category();
        category.setName(req.getName());
        category.setImage(imagePath);
        categoryRepository.save(category);
        return new ApiResponse<>("Category created", HttpStatus.CREATED.value());
    }

    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryDto::toDto);
    }
}
