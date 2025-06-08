package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.CategoryDto;
import com.tekerasoft.tekeramarketplace.dto.CompanyAdminDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreateSubCategoryRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.CategoryService;
import com.tekerasoft.tekeramarketplace.service.CompanyService;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import com.tekerasoft.tekeramarketplace.service.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/super-admin")
public class SuperAdminController {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final CompanyService companyService;
    private final ProductService productService;

    public SuperAdminController(CategoryService categoryService, SubCategoryService subCategoryService,
                                CompanyService companyService, ProductService productService) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.companyService = companyService;
        this.productService = productService;
    }

    @PostMapping(value = "/create-category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createCategory(@Valid @ModelAttribute CreateCategoryRequest req) {
        return categoryService.save(req);
    }

    @PostMapping(value = "/create-sub-category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createSubCategory(@Valid @ModelAttribute CreateSubCategoryRequest req) {
        return subCategoryService.createSubCategory(req);
    }

    @GetMapping("/get-all-category")
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        return categoryService.getAllCategory(pageable);
    }

    @GetMapping("/get-all-company")
    public Page<CompanyAdminDto> getAllCompany(Pageable pageable) {
        return companyService.getAllCompanies(pageable);
    }

    @DeleteMapping("/delete-category")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@RequestParam("categoryId") String categoryId) {
           return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@RequestParam("productId") String productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }
}
