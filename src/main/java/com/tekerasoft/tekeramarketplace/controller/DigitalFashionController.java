package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/digital-fashion")
public class DigitalFashionController {
    private final DigitalFashionService digitalFashionService;

    public DigitalFashionController(DigitalFashionService digitalFashionService) {
        this.digitalFashionService = digitalFashionService;
    }

}
