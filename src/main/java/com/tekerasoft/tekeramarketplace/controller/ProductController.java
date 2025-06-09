package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.request.FilterProductRequest;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<Page<ProductListDto>> getAllProduct(Pageable pageable) {
        return ResponseEntity.ok(productService.findAllListProduct(pageable));
    }

    @GetMapping("/getProductBySlug")
    public ResponseEntity<ProductDto> getProductBySlug(@RequestParam String slug) {
        return ResponseEntity.ok(productService.findBySlug(slug));
    }

    @GetMapping("/filterProduct")
    public ResponseEntity<Page<ProductListDto>> filterProduct(@RequestBody FilterProductRequest req, Pageable pageable)
    {
        return ResponseEntity.ok(productService.filterProducts(req,pageable));
    }

}
