package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.*;
import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/super-admin")
public class SuperAdminController {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final CompanyService companyService;
    private final ProductService productService;
    private final ThemeService themeService;
    private final FashionCollectionService fashionCollectionService;

    public SuperAdminController(CategoryService categoryService, SubCategoryService subCategoryService,
                                CompanyService companyService, ProductService productService,
                                ThemeService themeService, FashionCollectionService fashionCollectionService)
    {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.companyService = companyService;
        this.productService = productService;
        this.themeService = themeService;
        this.fashionCollectionService = fashionCollectionService;
    }

    @PostMapping(value = "/createCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createCategory(@Valid @ModelAttribute CreateCategoryRequest req) {
        return categoryService.save(req);
    }

    @PostMapping(value = "/createSubCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createSubCategory(@Valid @ModelAttribute CreateSubCategoryRequest req) {
        return subCategoryService.createSubCategory(req);
    }

    @GetMapping("/getCustomerProduct")
    public ResponseEntity<ProductDto> getCustomerProduct(@RequestParam String id) {
        return ResponseEntity.ok(productService.getCustomerProduct(id));
    }

    @GetMapping("/getAllCategory")
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        return categoryService.getAllCategory(pageable);
    }

    @GetMapping("/getAllCompany")
    public Page<CompanyAdminDto> getAllCompany(Pageable pageable) {
        return companyService.getAllCompanies(pageable);
    }

    @DeleteMapping("/deleteCategory")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@RequestParam("categoryId") String categoryId) {
           return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@RequestParam("productId") String productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @DeleteMapping("/deleteCompany")
    public ResponseEntity<ApiResponse<?>> deleteCompany(@RequestParam("companyId") String companyId) {
        return ResponseEntity.ok(companyService.deleteCompany(companyId));
    }

    @PutMapping("/changeCompanyStatusCode")
    public ResponseEntity<ApiResponse<?>> changeCompanyActiveStatus(@RequestParam("companyId") String companyId,
                                                                    @RequestParam("status") Boolean status) {
        return ResponseEntity.ok(companyService.changeCompanyActiveStatus(companyId,status));
    }

    @PutMapping("/changeProductActiveStatus")
    public ResponseEntity<ApiResponse<?>> changeProductActiveStatus(@RequestParam("productId") String productId,
                                                                    @RequestParam("status") Boolean status) {
        return ResponseEntity.ok(productService.changeProductActiveStatus(productId,status));
    }

    @GetMapping("/getAllAdminProduct")
    public ResponseEntity<Page<ProductDto>> getAllAdminProduct(Pageable pageable) {
        return ResponseEntity.ok(productService.findAllAdminProduct(pageable));
    }

    @GetMapping("/filterAdminProduct")
    public ResponseEntity<Page<ProductDto>> filterProduct(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String style,
            Pageable pageable) {
        return  ResponseEntity.ok(productService.filterAdminProduct(color,size,tags,style, pageable));
    }

    @PostMapping(value ="/createTheme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createTheme(@Valid @ModelAttribute CreateThemeRequest req) {
        return ResponseEntity.ok(themeService.createTheme(req));
    }

    @PutMapping(value = "/updateTheme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updateTheme(@Valid @ModelAttribute UpdateThemeRequest req) {
        return ResponseEntity.ok(themeService.updateTheme(req));
    }

    @DeleteMapping("/deleteTheme")
    public ResponseEntity<ApiResponse<?>> deleteTheme(@RequestParam String id) {
        return ResponseEntity.ok(themeService.deleteTheme(id));
    }

    @GetMapping("getAllTheme")
    public ResponseEntity<List<ThemeDto>> getAllTheme() {
        return ResponseEntity.ok(themeService.getAllTheme());
    }

    @GetMapping("/getAllFashionCollection")
    public ResponseEntity<Page<FashionCollectionDto>>  getAllFashionCollection(Pageable pageable) {
        return ResponseEntity.ok(fashionCollectionService.getAllFashionCollection(pageable));
    }

    @PostMapping(value ="/createFashionCollection", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createFashionCollection(@Valid @ModelAttribute CreateFashionCollectionRequest req) {
        return ResponseEntity.ok(fashionCollectionService.createFashionCollection(req));
    }

    @PutMapping(value = "/updateFashionCollection", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>>  updateFashionCollection(@Valid @ModelAttribute UpdateFashionCollectionRequest req) {
        return ResponseEntity.ok(fashionCollectionService.updateFashionCollection(req));
    }

    @DeleteMapping("/deleteFashionCollection")
    public ResponseEntity<ApiResponse<?>> deleteFashionCollection(@RequestParam("id") String id) {
        return ResponseEntity.ok(fashionCollectionService.deleteFashionCollection(id));
    }

    @GetMapping("/getFashionCollection")
    public ResponseEntity<FashionCollectionDto> getFashionCollection(@RequestParam String id) {
        return ResponseEntity.ok(fashionCollectionService.getFashionCollection(id));
    }
}