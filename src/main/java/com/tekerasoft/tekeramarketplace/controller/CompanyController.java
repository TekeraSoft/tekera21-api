package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping(value = "/create-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createProduct(@RequestPart("data") CreateProductRequest req,
                                                 @RequestPart(value = "images") List<MultipartFile> images)
    {
        return productService.create(req,images);
    }
}