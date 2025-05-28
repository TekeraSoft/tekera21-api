package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/digital-fashion")
public class DigitalFashionController {
    private final DigitalFashionService digitalFashionService;

    public DigitalFashionController(DigitalFashionService digitalFashionService) {
        this.digitalFashionService = digitalFashionService;
    }

    public ApiResponse<?> createTargetPicture(@ModelAttribute CreateTargetPictureRequest req) {
        return digitalFashionService.createTargetPicture(req);
    }
}
