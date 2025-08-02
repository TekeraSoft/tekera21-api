package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.SellerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/account")
public class AccountController {

    private final SellerService sellerService;

    public AccountController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("/createSeller")
    public ApiResponse<?> createCompany(@RequestPart("data") CreateCompanyRequest req,
                                                        @RequestPart("files") List<MultipartFile> files,
                                                        @RequestPart("logo") MultipartFile logo)
    {
        return sellerService.createSeller(req,files,logo);
    }
}
