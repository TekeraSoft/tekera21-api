package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/account")
public class AccountController {

    private final CompanyService companyService;

    public AccountController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/create-company")
    public ResponseEntity<ApiResponse<?>> createCompany(@RequestPart("data") CreateCompanyRequest req,
                                                        @RequestPart("files") List<MultipartFile> files,
                                                        @RequestPart("logo") MultipartFile logo)
    {
        return ResponseEntity.ok(companyService.createCompany(req,files,logo));
    }


}
