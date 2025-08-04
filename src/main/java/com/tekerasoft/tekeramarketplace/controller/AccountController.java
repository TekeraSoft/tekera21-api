package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.ShippingCompanyDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany;
import com.tekerasoft.tekeramarketplace.service.SellerService;
import com.tekerasoft.tekeramarketplace.service.ShippingCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/account")
public class AccountController {

    private final SellerService sellerService;
    private final ShippingCompanyService shippingCompanyService;

    public AccountController(SellerService sellerService, ShippingCompanyService shippingCompanyService) {
        this.sellerService = sellerService;
        this.shippingCompanyService = shippingCompanyService;
    }

    @PostMapping("/createSeller")
    public ApiResponse<?> createCompany(@RequestPart("data") CreateCompanyRequest req,
                                                        @RequestPart("files") List<MultipartFile> files,
                                                        @RequestPart("logo") MultipartFile logo)
    {
        return sellerService.createSeller(req,files,logo);
    }

    @GetMapping("/getAllShippingCompany")
    public ResponseEntity<List<ShippingCompanyDto>> getAllShippingCompany() {
        return ResponseEntity.ok(shippingCompanyService.getAllShippingCompany());
    }
}
