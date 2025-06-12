package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.TargetPictureDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getTargetImageByProductId")
    public ResponseEntity<TargetPictureDto> getTargetPictureByProductId(@RequestParam String productId) {
        return ResponseEntity.ok(digitalFashionService.getTargetPictureByProductId(productId));
    }

    @GetMapping("/getByTargetId")
    public ResponseEntity<TargetPictureDto> getTargetPictureByTargetId(@RequestParam String targetId) {
        return ResponseEntity.ok(digitalFashionService.getTargetPictureById(targetId));
    }

}
