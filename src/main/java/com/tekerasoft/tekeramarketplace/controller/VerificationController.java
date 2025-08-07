package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.ShippingCompanyDto;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateSellerRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.SellerService;
import com.tekerasoft.tekeramarketplace.service.ShippingCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/verification")
public class VerificationController {
    private final SellerService sellerService;
    private final ShippingCompanyService shippingCompanyService;
    public VerificationController(SellerService sellerService, ShippingCompanyService shippingCompanyService) {
        this.sellerService = sellerService;
        this.shippingCompanyService = shippingCompanyService;
    }

    @PutMapping("/updateSeller")
    public ResponseEntity<ApiResponse<?>> updateSeller(@RequestPart("data") UpdateSellerRequest req,
                                                       @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                                       @RequestPart(name = "logo", required = false) MultipartFile logo) {
        return ResponseEntity.ok(sellerService.updateSeller(req,files,logo));
    }
}
