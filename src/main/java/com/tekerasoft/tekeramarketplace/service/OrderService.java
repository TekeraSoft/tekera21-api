package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.repository.jparepository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
