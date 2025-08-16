package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.FashionCollectionDto;
import com.tekerasoft.tekeramarketplace.dto.FashionCollectionListDto;
import com.tekerasoft.tekeramarketplace.service.FashionCollectionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/fashion-collection")
public class FashionCollectionController {

    private final FashionCollectionService fashionCollectionService;

    public FashionCollectionController(FashionCollectionService fashionCollectionService) {
        this.fashionCollectionService = fashionCollectionService;
    }

    @GetMapping("/getAllFashionCollection")
    public ResponseEntity<Page<FashionCollectionListDto>> getAllFashionCollection(Pageable pageable) {
        return ResponseEntity.ok(fashionCollectionService.getAllFashionCollection(pageable));
    }

    @GetMapping("getFashionCollectionsByCompany")
    public ResponseEntity<Page<FashionCollectionListDto>>  getFashionCollectionsByCompany(@RequestParam String companyId,
                                                                                      Pageable pageable) {
        return ResponseEntity.ok(fashionCollectionService.getFashionCollectionsByCompany(companyId, pageable));
    }

    @GetMapping("/getFashionCollection")
    public ResponseEntity<FashionCollectionDto> getFashionCollection(@RequestParam String id) {
        return ResponseEntity.ok(fashionCollectionService.getFashionCollection(id));
    }

}