package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ShippingCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShippingCompanyService {

    private final ShippingCompanyRepository shippingCompanyRepository;

    public ShippingCompanyService(ShippingCompanyRepository shippingCompanyRepository) {
        this.shippingCompanyRepository = shippingCompanyRepository;
    }

    public ShippingCompany getShippingCompany(String id) {
        return shippingCompanyRepository.findById(UUID.fromString(id))
                .orElseThrow();
    }

}
