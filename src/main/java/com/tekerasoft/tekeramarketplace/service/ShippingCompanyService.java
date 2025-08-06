package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ShippingCompanyDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateShippingCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ShippingCompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShippingCompanyService {

    private final ShippingCompanyRepository shippingCompanyRepository;

    public ShippingCompanyService(ShippingCompanyRepository shippingCompanyRepository) {
        this.shippingCompanyRepository = shippingCompanyRepository;
    }

    public ShippingCompany getShippingCompany(String id) {
        return shippingCompanyRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Shipping Company not found"));
    }

    public List<ShippingCompanyDto> getAllShippingCompany() {
        return shippingCompanyRepository.findAll().stream().map(ShippingCompanyDto::toDto).collect(Collectors.toList());
    }

    public Set<ShippingCompany> getShippingCompanies(List<String> ids) {
        List<UUID> uuidList = ids.stream()
                .map(UUID::fromString) // String → UUID
                .toList();
        return new HashSet<>(shippingCompanyRepository.findAllById(uuidList));
    }

    public ApiResponse<?> createShippingCompany(CreateShippingCompanyRequest req) {
        ShippingCompany shippingCompany = new ShippingCompany();
        shippingCompany.setName(req.getName());
        shippingCompany.setGsmNumber(req.getGsmNumber());
        shippingCompany.setEmail(req.getEmail());
        shippingCompany.setPrice(req.getPrice());
        shippingCompany.setMinDeliveryDay(req.getMinDeliveryDay());
        shippingCompany.setMaxDeliveryDay(req.getMaxDeliveryDay());

        shippingCompanyRepository.save(shippingCompany);
        return new ApiResponse<>("Shipping Company Created", HttpStatus.OK.value());
    }

}
