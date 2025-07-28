package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.entity.Address;
import com.tekerasoft.tekeramarketplace.model.entity.BasketItem;
import com.tekerasoft.tekeramarketplace.model.entity.Buyer;
import com.tekerasoft.tekeramarketplace.repository.jparepository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final AttributeService attributeService;
    private final ProductService productService;
    private final VariationService variationService;

    public OrderService(OrderRepository orderRepository,
                        UserService userService,
                        AttributeService attributeService,
                        ProductService productService,
                        VariationService variationService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.attributeService = attributeService;
        this.productService = productService;
        this.variationService = variationService;
    }
    
    public Order createOrder(CreateOrderRequest req) {
        Order order = new Order();

        Optional<User> user = userService.getByUsername(req.getBuyer().getEmail());
        user.ifPresent(order::setUser);

        List<BasketItem> basketItems = req.getBasketItems().stream().map(bi -> {
            Product product = productService.getById(UUID.fromString(bi.getProductId()));
            Attribute attribute = attributeService.getAttributeById(bi.getAttributeId());
            Variation variation = variationService.getVariation(bi.getVariationId());
            return new BasketItem(
                    UUID.fromString(bi.getProductId()),
                    product.getName(),
                    product.getCode(),
                    product.getBrandName(),
                    bi.getQuantity(),
                    variation.getModelCode(),
                    attribute.getPrice(),
                    attribute.getSku(),
                    attribute.getBarcode(),
                    variation.getImages().get(0),
                    bi.getAttributeId(),
                    attribute.getAttributeDetails().stream().map(it ->
                            new BasketAttributes(it.getKey(),it.getValue())).toList(),
                    product.getCompany().getShippingCompanies().stream().findFirst().get().getPrice(),
                    product.getCompany(),
                    product.getCompany().getShippingCompanies().stream().findFirst().get()
            );
        }).toList();
        order.setBasketItems(basketItems);

        Buyer buyer = new Buyer();
        buyer.setName(req.getBuyer().getName());
        buyer.setSurname(req.getBuyer().getSurname());
        buyer.setEmail(req.getBuyer().getEmail());
        buyer.setGsmNumber(req.getBuyer().getGsmNumber());
        buyer.setIdentityNumber(req.getBuyer().getIdentityNumber());
        buyer.setRegistered(user.isPresent());
        order.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setCity(req.getShippingAddress().getCity());
        shippingAddress.setCountry(req.getShippingAddress().getCountry());
        shippingAddress.setPostalCode(req.getShippingAddress().getPostalCode());
        shippingAddress.setStreet(req.getShippingAddress().getStreet());
        shippingAddress.setBuildNo(req.getShippingAddress().getBuildNo());
        shippingAddress.setDoorNumber(req.getShippingAddress().getDoorNumber());
        shippingAddress.setDetailAddress(req.getShippingAddress().getDetailAddress());
        order.setShippingAddress(shippingAddress);

        if(req.getBillingAddress() != null) {
            Address billingAddress = new Address();
            billingAddress.setCity(req.getBillingAddress().getCity());
            billingAddress.setCountry(req.getBillingAddress().getCountry());
            billingAddress.setPostalCode(req.getBillingAddress().getPostalCode());
            billingAddress.setStreet(req.getBillingAddress().getStreet());
            billingAddress.setBuildNo(req.getBillingAddress().getBuildNo());
            billingAddress.setDoorNumber(req.getBillingAddress().getDoorNumber());
            billingAddress.setDetailAddress(req.getBillingAddress().getDetailAddress());
            order.setBillingAddress(billingAddress);
        } else {
            order.setBillingAddress(shippingAddress);
        }

        order.setTotalPrice(req.getTotalPrice());
        order.setPaymentStatus(req.getPaymentStatus());
        order.setPaymentType(req.getPaymentType());

        return orderRepository.save(order);
    }

    @Transactional
    public void completeOrder(String conversationId,PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(UUID.fromString(conversationId))
                        .orElseThrow(() -> new NotFoundException("Order not found"));
        if(paymentStatus.equals(PaymentStatus.PAID)) {
            order.setPaymentStatus(paymentStatus);
            for(BasketItem bi: order.getBasketItems()) {
                attributeService.decreaseStock(bi.getAttributeId(), bi.getQuantity());
            }
            orderRepository.save(order);
        }
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

}
