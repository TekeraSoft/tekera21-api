package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.exception.DecreaseStockException;
import com.tekerasoft.tekeramarketplace.model.entity.Attribute;
import com.tekerasoft.tekeramarketplace.repository.jparepository.AttributeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AttributeService {
    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public Attribute getAttributeById(String id) {
        return attributeRepository.findById(UUID.fromString(id)).orElse(null);
    }

    public void decreaseStock(String attributeId, int quantity) {
        try {
            attributeRepository.decreaseStock(UUID.fromString(attributeId),quantity);
        } catch (RuntimeException e) {
            throw new DecreaseStockException("Product sold out");
        }
    }
}
