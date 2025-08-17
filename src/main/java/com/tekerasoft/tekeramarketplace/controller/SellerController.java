package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.*;
import com.tekerasoft.tekeramarketplace.dto.request.CreateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateProductRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/seller")
public class SellerController {
    private final ProductService productService;
    private final SellerService sellerService;
    private final FileService fileService;
    private final VariationService variationService;
    private final DigitalFashionService digitalFashionService;
    private final SellerOrderService sellerOrderService;
    private final OrderService orderService;

    public SellerController(ProductService productService, SellerService sellerService, FileService fileService,
                            VariationService variationService, DigitalFashionService digitalFashionService,
                            SellerOrderService sellerOrderService, OrderService orderService) {
        this.productService = productService;
        this.sellerService = sellerService;
        this.fileService = fileService;
        this.variationService = variationService;
        this.digitalFashionService = digitalFashionService;
        this.sellerOrderService = sellerOrderService;
        this.orderService = orderService;
    }

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

    @GetMapping("/sellerProducts")
    public ResponseEntity<Page<ProductListDto>> findCompanyReturnProducts(Pageable pageable) {
        return ResponseEntity.ok(sellerService.getSellerProducts(pageable));
    }

    @GetMapping("/getProductById")
    public ResponseEntity<ProductDto> getProductById(@RequestParam String productId) {
        return ResponseEntity.ok(productService.getCustomerProduct(productId));
    }

    @DeleteMapping("/deleteProductById")
    public ApiResponse<?> deleteProductById(@RequestParam String productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping("/sellerGallery")
    public ResponseEntity<Page<String>> sellerGallery(@RequestParam String companyId, Pageable pageable)
            throws Exception {
        return ResponseEntity.ok(sellerService.getAllCompanyMedia(companyId,pageable));
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

    @GetMapping("findOrdersByPhoneNumberOrUsername")
    public ResponseEntity<List<SellerOrderDto>> findOrdersByPhoneNumberOrUsername(@RequestParam String searchParam) {
        return ResponseEntity.ok(sellerOrderService.findOrdersByPhoneNumberOrUsername(searchParam));
    }

    @GetMapping("/getSellerProducts")
    public ResponseEntity<Page<ProductDto>> getSellerProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.findProductBySeller(pageable));
    }

    @GetMapping("/getProductBySellerCheck")
    public ResponseEntity<ProductDto> getProductBySellerCheck(@RequestParam String productId) {
        return ResponseEntity.ok(productService.getProductBySeller(productId));
    }

    @GetMapping("/getSellerInformation")
    public ResponseEntity<SellerAdminDto> getSellerByUserId() {
        return ResponseEntity.ok(sellerService.getSellerInformation());
    }

    @GetMapping("/getSellerOrders")
    public ResponseEntity<Page<OrderDto>> findSellerOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getSellerOrders(pageable));
    }

    @GetMapping("/getSellerReport")
    public ResponseEntity<SellerReportAggregation> getSellerReportBySellerUserId() {
        return ResponseEntity.ok(sellerService.getSellerReportBySellerUserId());
    }

}