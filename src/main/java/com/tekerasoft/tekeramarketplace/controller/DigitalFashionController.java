package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.document.TargetPictureDto;
import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/digital-fashion")
public class DigitalFashionController {
    private final DigitalFashionService digitalFashionService;

    public DigitalFashionController(DigitalFashionService digitalFashionService) {
        this.digitalFashionService = digitalFashionService;
    }

    @GetMapping("/get-all-target-pic")
    public Page<TargetPictureDto> getAllTargetPictures(Pageable pageable) {
        return digitalFashionService.getAllTargetPictures(pageable);
    }
}
