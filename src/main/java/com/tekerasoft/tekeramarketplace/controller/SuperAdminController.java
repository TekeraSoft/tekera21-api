package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.CategoryDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreateSubCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.CategoryService;
import com.tekerasoft.tekeramarketplace.service.SubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/super-admin")
public class SuperAdminController {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    public SuperAdminController(CategoryService categoryService, SubCategoryService subCategoryService) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
    }

    @PostMapping("/create-category")
    public ResponseEntity<ApiResponse<?>> createCategory(@ModelAttribute CreateCategoryRequest req) {
        return ResponseEntity.ok(categoryService.save(req));
    }

    @PostMapping("/create-sub-category")
    public ResponseEntity<ApiResponse<?>> createSubCategory(@ModelAttribute CreateSubCategoryRequest req) {
        return ResponseEntity.ok(subCategoryService.createSubCategory(req));
    }

    @GetMapping("/get-all-category")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }
}
