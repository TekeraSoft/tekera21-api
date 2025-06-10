package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ApiResponse<?> createProduct(@RequestPart("data") CreateProductRequest req,
                                                 @RequestPart(value = "images") List<MultipartFile> images)
    {
        return productService.create(req,images);
    }

    @GetMapping("/findCompanyReturnProducts/{companyId}")
    public ResponseEntity<Page<ProductListDto>> findCompanyReturnProducts(@PathVariable String companyId, Pageable pageable) {
        return ResponseEntity.ok(productService.findCompanyReturnProducts(companyId,pageable));
    }

}