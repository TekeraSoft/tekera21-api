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
    private final SellerService sellerService;
    private final ProductService productService;
    private final ThemeService themeService;
    private final FashionCollectionService fashionCollectionService;
    private final ShippingCompanyService shippingCompanyService;
    private final UserService userService;
    private final SellerOrderService sellerOrderService;

    public SuperAdminController(CategoryService categoryService, SubCategoryService subCategoryService,
                                SellerService sellerService, ProductService productService,
                                ThemeService themeService, FashionCollectionService fashionCollectionService,
                                ShippingCompanyService shippingCompanyService, UserService userService,
                                SellerOrderService sellerOrderService)
    {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.sellerService = sellerService;
        this.productService = productService;
        this.themeService = themeService;
        this.fashionCollectionService = fashionCollectionService;
        this.shippingCompanyService = shippingCompanyService;
        this.userService = userService;
        this.sellerOrderService = sellerOrderService;
    }

    @PostMapping(value = "/createCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createCategory(@Valid @ModelAttribute CreateCategoryRequest req) {
        return categoryService.save(req);
    }

    @PostMapping(value = "/createSubCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createSubCategory(@Valid @ModelAttribute CreateSubCategoryRequest req) {
        return subCategoryService.createSubCategory(req);
    }

    @PutMapping("/activeUserSellerRole")
    public ResponseEntity<ApiResponse<?>> activeUserSellerRole(@RequestBody SellerVerificationRequest req) {
        return ResponseEntity.ok(userService.activeUserSellerRole(req));
    }

    @GetMapping("/getSellerProducts")
    public ResponseEntity<ProductDto> getCustomerProduct(@RequestParam String id) {
        return ResponseEntity.ok(productService.getCustomerProduct(id));
    }

    @GetMapping("/getAllCategory")
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        return categoryService.getAllCategory(pageable);
    }

    @GetMapping("/getAllCompany")
    public Page<SellerAdminDto> getAllCompany(Pageable pageable) {
        return sellerService.getAllCompanies(pageable);
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
        return ResponseEntity.ok(sellerService.deleteCompany(companyId));
    }

    @PutMapping("/changeCompanyStatusCode")
    public ResponseEntity<ApiResponse<?>> changeCompanyActiveStatus(@RequestParam("companyId") String companyId) {
        return ResponseEntity.ok(sellerService.changeSellerActiveStatus(companyId));
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
            @RequestParam(required = false) String subCategoryName,
            @RequestParam(required = false) String searchParam,
            Pageable pageable) {
        return  ResponseEntity.ok(productService.filterAdminProduct(color,size,tags,style,subCategoryName,searchParam,pageable));
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
    public ResponseEntity<Page<FashionCollectionListDto>>  getAllFashionCollection(Pageable pageable) {
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

    @PostMapping("/createShippingCompany")
    public ResponseEntity<ApiResponse<?>> createShippingCompany(@RequestBody CreateShippingCompanyRequest req) {
        return ResponseEntity.ok(shippingCompanyService.createShippingCompany(req));
    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<Page<SellerOrderDto>> getAllOrder(Pageable pageable) {
        return ResponseEntity.ok(sellerOrderService.getAllOrder(pageable));
    }
}