package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.ProductUiDto;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<Page<ProductUiDto>> getAllProduct(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllLProduct(pageable));
    }

    @GetMapping("/getProductBySlug")
    public ResponseEntity<ProductDto> getProductBySlug(@RequestParam String slug) {
        return ResponseEntity.ok(productService.findBySlug(slug));
    }

    @GetMapping("/getProductById")
    public ResponseEntity<ProductListDto> getProductById(@RequestParam String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/findCompanyReturnProducts")
    public ResponseEntity<Page<ProductListDto>> findCompanyReturnProducts(@RequestParam String companyId,
                                                                          Pageable pageable) {
        return ResponseEntity.ok(productService.findCompanyReturnProducts(companyId,pageable));
    }

    @GetMapping("/findCompanyPopularOrNewSeasonProducts")
    public ResponseEntity<Page<ProductUiDto>> findCompanySeasonProduct(@RequestParam String companyId,
                                                                          @RequestParam String tag,
                                                                          Pageable pageable) {
        return ResponseEntity.ok(productService.findCompanyPopularOrNewSeasonProducts(companyId,tag,pageable));
    }

    @GetMapping("/findCompanyReturnProductDetail")
    public ResponseEntity<ProductDto> findCompanyReturnProduct(@RequestParam String companyId,
                                                               @RequestParam String slug) {
        return ResponseEntity.ok(productService.findCompanyReturnProduct(companyId, slug));
    }

    @GetMapping("/filterProduct")
    public ResponseEntity<Page<ProductUiDto>> filterProduct(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String clothSize,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String subCategoryName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String searchParam,
            Pageable pageable) {
        return  ResponseEntity.ok(productService.filterProduct(color,clothSize,tags,style,subCategoryName,categoryName,searchParam,pageable));
    }

    @GetMapping("/getProductsByCategory")
    public ResponseEntity<Page<ProductUiDto>> getProductsByCategory(@RequestParam String categoryName,Pageable pageable) {
        return ResponseEntity.ok(productService.getAllLProduct(pageable));
    }

}
