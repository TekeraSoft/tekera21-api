package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.SellerDocument;
import com.tekerasoft.tekeramarketplace.model.entity.Variation;
import com.tekerasoft.tekeramarketplace.repository.jparepository.VariationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class VariationService {

    private final VariationRepository variationRepository;
    private final FileService fileService;

    public VariationService(VariationRepository variationRepository, FileService fileService) {
        this.variationRepository = variationRepository;
        this.fileService = fileService;
    }

    public ApiResponse<?> delteImageFromVariant(String path) {
        Variation variation = variationRepository.findByImagePath(path)
                .orElseThrow(() -> new RuntimeException("No image"));

        List<String> images = new ArrayList<>(variation.getImages());

        if (images.remove(path)) {
            variation.setImages(images);
            variationRepository.save(variation);
            fileService.deleteInFolderFile(path);
        }

        return new ApiResponse<>("Delete Variation Image", HttpStatus.OK.value());
    }

    public Variation getVariation(String id) {
        return variationRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Variation not found"));
    }
}
