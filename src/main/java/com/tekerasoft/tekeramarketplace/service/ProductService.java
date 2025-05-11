package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


}
