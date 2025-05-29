package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/digital-fashion-admin")
public class DigitalFashionAdminController {

    private final DigitalFashionService digitalFashionService;

    public DigitalFashionAdminController(DigitalFashionService digitalFashionService) {
        this.digitalFashionService = digitalFashionService;
    }

    @PostMapping("/create-target-pic")
    public ApiResponse<?> createTargetPicture(@Valid @ModelAttribute CreateTargetPictureRequest req) {
        return digitalFashionService.createTargetPicture(req);
    }
}
