package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.TargetPictureDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-target-pic")
    public ResponseEntity<TargetPictureDto> getTargetPicture(@RequestParam("productId") String productId,
                                                             @RequestParam("targetId") String targetId) {
        return ResponseEntity.ok(digitalFashionService.getTargetPictureAndContent(productId, targetId));
    }

    @DeleteMapping("/delete-target-pic")
    public ApiResponse<?> deleteTargetPicture(String id) {
        return digitalFashionService.deleteTargetPicture(id);
    }
}
