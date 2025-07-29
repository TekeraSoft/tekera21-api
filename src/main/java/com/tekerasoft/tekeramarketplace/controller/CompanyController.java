package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.ProductDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.TargetPictureDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.*;
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
    private final CompanyService companyService;
    private final FileService fileService;
    private final VariationService variationService;
    private final DigitalFashionService digitalFashionService;
    private final OrderService orderService;

    public CompanyController(ProductService productService, CompanyService companyService, FileService fileService, VariationService variationService, DigitalFashionService digitalFashionService, OrderService orderService) {
        this.productService = productService;
        this.companyService = companyService;
        this.fileService = fileService;
        this.variationService = variationService;
        this.digitalFashionService = digitalFashionService;
        this.orderService = orderService;
    }

    @Operation(summary = "Company create product action")
    @PostMapping(value = "/createProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createProduct(@Valid @RequestPart("data") CreateProductRequest req,
                                                 @RequestPart(value = "images") List<MultipartFile> images)
    {
        return productService.create(req,images);
    }

    @PutMapping(value = "/updateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateProduct(@RequestPart("data") UpdateProductRequest req,
                                        @RequestPart(value = "images", required = false) List<MultipartFile> images){
        return productService.update(req,images);
    }

    @GetMapping("/findCompanyReturnProducts/{companyId}")
    public ResponseEntity<Page<ProductListDto>> findCompanyReturnProducts(@PathVariable String companyId, Pageable pageable) {
        return ResponseEntity.ok(productService.findCompanyReturnProducts(companyId,pageable));
    }

    @GetMapping("/getProductById")
    public ResponseEntity<ProductDto> getProductById(@RequestParam String productId) {
        return ResponseEntity.ok(productService.getCustomerProduct(productId));
    }

    @GetMapping("/sellerGallery")
    public ResponseEntity<Page<String>> sellerGallery(@RequestParam String companyId, Pageable pageable) throws Exception {
        return ResponseEntity.ok(companyService.getAllCompanyMedia(companyId,pageable));
    }

    @GetMapping("/getPresignedUrl")
    public ResponseEntity<String>  getPresignedUrl(@RequestParam String objectName) {
        return ResponseEntity.ok(fileService.generatePresignedUploadUrl(objectName));
    }

    @DeleteMapping("/deleteImageFromVariation")
    public ResponseEntity<ApiResponse<?>> deleteImageFromVariation(@RequestParam String path) {
        return ResponseEntity.ok(variationService.delteImageFromVariant(path));
    }

    @PostMapping("/createTargetPic")
    public ApiResponse<?> createTargetPicture(@Valid @ModelAttribute CreateTargetPictureRequest req) {
        return digitalFashionService.createTargetPicture(req);
    }

    @GetMapping("/getTargetPic")
    public ResponseEntity<TargetPictureDto> getTargetPicture(@RequestParam("productId") String productId,
                                                             @RequestParam("targetId") String targetId) {
        return ResponseEntity.ok(digitalFashionService.getTargetPictureAndContent(productId, targetId));
    }

    @DeleteMapping("/deleteTargetPic")
    public ApiResponse<?> deleteTargetPicture(@RequestParam String id) {
        return digitalFashionService.deleteTargetPicture(id);
    }

    @GetMapping("/findOrdersContainingBasketItemsForCompany")
    public ResponseEntity<Page<OrderDto>> findOrdersContainingBasketItemsForCompany(@RequestParam String companyId,
                                                                                    Pageable pageable)
    {
        return ResponseEntity.ok(orderService.findOrdersContainingBasketItemsForCompany(companyId,pageable));
    }

    @GetMapping("findOrdersByPhoneNumberOrUsername")
    public ResponseEntity<List<OrderDto>>  findOrdersByPhoneNumberOrUsername(@RequestParam String searchParam) {
        return ResponseEntity.ok(orderService.findOrdersByPhoneNumberOrUsername(searchParam));
    }
}