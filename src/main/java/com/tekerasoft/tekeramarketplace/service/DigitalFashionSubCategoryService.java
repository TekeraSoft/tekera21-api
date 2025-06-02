package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.repository.releational.DigitalFashionCategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.releational.DigitalFashionSubCategoryRepository;
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
