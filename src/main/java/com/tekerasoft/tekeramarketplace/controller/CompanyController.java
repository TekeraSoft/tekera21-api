package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/company")
@Tag(name = "Products", description = "Product management APIs")
public class CompanyController {
    private final ProductService productService;

    public CompanyController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Company create product action")
    @PostMapping(value = "/createProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createProduct(@Valid @RequestPart("data") CreateProductRequest req,
                                                 @RequestPart(value = "video", required = false) MultipartFile video,
                                                 @RequestPart(value = "images") List<MultipartFile> images)
    {
        return productService.create(req,video,images);
    }

    @PutMapping(value = "/updateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateProduct(@RequestPart("data") UpdateProductRequest req,
                                        @RequestPart(value = "video", required = false)  MultipartFile video,
                                        @RequestPart(value = "images", required = false) List<MultipartFile> images){
        return productService.update(req,video,images);
    }

    @GetMapping("/findCompanyReturnProducts/{companyId}")
    public ResponseEntity<Page<ProductListDto>> findCompanyReturnProducts(@PathVariable String companyId, Pageable pageable) {
        return ResponseEntity.ok(productService.findCompanyReturnProducts(companyId,pageable));
    }

    @GetMapping("/getProductById")
    public ResponseEntity<ProductDto> getProductById(@RequestParam String productId) {
        return ResponseEntity.ok(productService.getCustomerProduct(productId));
    }

}