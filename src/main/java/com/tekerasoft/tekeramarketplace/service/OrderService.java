package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.repository.jparepository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ShippingCompanyService shippingCompanyService;
    private final CompanyService companyService;

    public OrderService(OrderRepository orderRepository,
                        UserService userService,
                        ShippingCompanyService shippingCompanyService, CompanyService companyService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.shippingCompanyService = shippingCompanyService;
        this.companyService = companyService;
    }

    public ApiResponse<?> createOrder(CreateOrderRequest req) {
        Order order = new Order();

        Optional<User> user = userService.getByUsername(req.getBuyer().getEmail());
        user.ifPresent(order::setUser);

        Buyer buyerRequest = BuyerRequest.toEntity(req.getBuyer(), user.isPresent());
        order.setBuyer(buyerRequest);

        List<BasketItem> basketItems = req.getBasketItems().stream().map(BasketItemRequest::toEntity).toList();
        order.setBasketItems(basketItems);

        Address shippingAddressRequest = ShippingAddressRequest.toEntity(req.getShippingAddress());
        order.setShippingAddress(shippingAddressRequest);

        Address billingAddressRequest = BillingAddressRequest.toEntity(req.getBillingAddress());
        order.setBillingAddress(billingAddressRequest);

        order.setTotalPrice(req.getTotalPrice());
        order.setPaymentType(req.getPaymentType());
        order.setPaymentStatus(req.getPaymentStatus());

        order.setShippingCompany(shippingCompanyService.getShippingCompany(req.getShippingCompanyId()));
        orderRepository.save(order);
        return new ApiResponse<>("Order Created", HttpStatus.CREATED.value());
    }

}
