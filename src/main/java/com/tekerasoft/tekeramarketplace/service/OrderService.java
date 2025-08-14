package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.entity.Address;
import com.tekerasoft.tekeramarketplace.model.entity.BasketItem;
import com.tekerasoft.tekeramarketplace.model.entity.Buyer;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentType;
import com.tekerasoft.tekeramarketplace.repository.jparepository.OrderRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SellerOrderRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final SellerOrderService sellerOrderService;
    private final AuthenticationFacade authenticationFacade;

    public OrderService(OrderRepository orderRepository, SellerOrderService sellerOrderService, AuthenticationFacade authenticationFacade) {
        this.orderRepository = orderRepository;
        this.sellerOrderService = sellerOrderService;
        this.authenticationFacade = authenticationFacade;
    }

    public Order createOrder(String orderNumber,
                             CreatePayRequest req,
                             BigDecimal totalPrice,
                             PaymentType paymentType,
                             PaymentStatus paymentStatus) {

        List<SellerOrder> sellerOrders = sellerOrderService.createSellerOrder(
                CreateOrderRequest.convertFromPayRequestToOrderRequest(
                        req,
                        totalPrice,
                        paymentType,
                        paymentStatus
                )
        );

        Order order = new Order();
        order.setOrderNo(orderNumber);
        order.setShippingPrice(sellerOrders.stream().map(SellerOrder::getShippingPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setTotalPrice(totalPrice);
        order.setSellerOrder(sellerOrders);

       return orderRepository.save(order);
    }

    public Page<OrderDto> getSellerOrders(Pageable pageable) {
        return orderRepository
                .getSellerOrdersByUserId(UUID.fromString(authenticationFacade.getCurrentUserId()),pageable)
                .map(OrderDto::toDto);
    }

    public Order getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

}
