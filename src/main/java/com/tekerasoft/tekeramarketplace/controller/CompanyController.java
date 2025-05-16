package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.CompanyService;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/company")
public class CompanyController {
    private final ProductService productService;
    private final CompanyService companyService;

    public CompanyController(ProductService productService, CompanyService companyService) {
        this.productService = productService;
        this.companyService = companyService;
    }

    @PostMapping("/create-product")
    public ResponseEntity<ApiResponse<?>> createProduct(@RequestPart("data") CreateProductRequest req,
                                                 @RequestPart(value = "images") List<MultipartFile> images)
    {
        return ResponseEntity.ok(productService.create(req,images));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCompany(@RequestPart("data") CreateCompanyRequest req,
                                                        @RequestPart("file") List<MultipartFile> files,
                                                        @RequestPart("logo") MultipartFile logo)
    {
        return ResponseEntity.ok(companyService.createCompany(req,files,logo));
    }
}