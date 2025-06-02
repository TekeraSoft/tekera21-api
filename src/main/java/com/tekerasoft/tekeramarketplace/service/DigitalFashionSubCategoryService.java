package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.repository.DigitalFashionCategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.DigitalFashionSubCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class DigitalFashionSubCategoryService {
    private final DigitalFashionCategoryRepository digitalFashionCategoryRepository;
    private final DigitalFashionSubCategoryRepository digitalFashionSubCategoryRepository;

    public DigitalFashionSubCategoryService(DigitalFashionCategoryRepository digitalFashionCategoryRepository,
                                            DigitalFashionSubCategoryRepository digitalFashionSubCategoryRepository) {
        this.digitalFashionCategoryRepository = digitalFashionCategoryRepository;
        this.digitalFashionSubCategoryRepository = digitalFashionSubCategoryRepository;
    }
}
