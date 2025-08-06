package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.SellerVerificationSupportDto;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.enums.SellerDocument;
import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus;
import com.tekerasoft.tekeramarketplace.service.SellerService;
import com.tekerasoft.tekeramarketplace.service.SellerVerificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/seller-support")
public class SellerSupportController {
    private final SellerVerificationService sellerVerificationService;
    private final SellerService sellerService;

    public SellerSupportController(SellerVerificationService sellerVerificationService, SellerService sellerService) {
        this.sellerVerificationService = sellerVerificationService;
        this.sellerService = sellerService;
    }

    @GetMapping("/getNewSellerPageable")
    public ResponseEntity<Page<SellerVerificationSupportDto>> getSupportVerificationList(Pageable pageable) {
        return ResponseEntity.ok(sellerVerificationService.getSupportVerificationList(pageable));
    }

    @PutMapping("/changeStatusFaultyDocument")
    public ResponseEntity<ApiResponse<?>> changeStatusFaultyDocument(@RequestParam String sellerId,
                                                                    @RequestParam SellerDocument sellerDocumentName,
                                                                    @RequestParam VerificationStatus status) {
        return ResponseEntity.ok(sellerService.changeStatusFaultyDocument(sellerId,sellerDocumentName,status));
    }
}
