package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.entity.Address;
import com.tekerasoft.tekeramarketplace.model.entity.BasketItem;
import com.tekerasoft.tekeramarketplace.model.entity.Buyer;
import com.tekerasoft.tekeramarketplace.repository.jparepository.OrderRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final AttributeService attributeService;
    private final ProductService productService;
    private final VariationService variationService;
    private final SellerService sellerService;
    private final AuthenticationFacade authenticationFacade;

    public OrderService(OrderRepository orderRepository,
                        UserService userService,
                        AttributeService attributeService,
                        ProductService productService,
                        VariationService variationService,
                        SellerService sellerService,
                        AuthenticationFacade authenticationFacade) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.attributeService = attributeService;
        this.productService = productService;
        this.variationService = variationService;
        this.sellerService = sellerService;
        this.authenticationFacade = authenticationFacade;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest req) {
        Order order = new Order();

        Optional<User> user = userService.getByUsername(authenticationFacade.getCurrentUserEmail());

        List<BasketItem> basketItems = req.getBasketItems().stream().map(bi -> {
            Product product = productService.getById(UUID.fromString(bi.getProductId()));
            Attribute attribute = attributeService.getAttributeById(bi.getAttributeId());
            Variation variation = variationService.getVariation(bi.getVariationId());
            BasketItem basketItem = new BasketItem();
            basketItem.setName(product.getName());
            basketItem.setProductId(product.getId().toString());
            basketItem.setVariationId(variation.getId().toString());
            basketItem.setSlug(product.getSlug());
            basketItem.setProductId(product.getId().toString());
            basketItem.setAttributeId(attribute.getId().toString());
            basketItem.setCode(product.getCode());
            basketItem.setBrandName(product.getBrandName());
            basketItem.setQuantity(bi.getQuantity());
            basketItem.setModelCode(variation.getModelCode());
            basketItem.setPrice(attribute.getPrice());
            basketItem.setSku(attribute.getSku());
            basketItem.setBarcode(attribute.getBarcode());
            basketItem.setImage(variation.getImages().get(0));
            basketItem.setAttributes(attribute.getAttributeDetails().stream().map(it ->
                    new BasketAttributes(it.getKey(),it.getValue())).toList());
            basketItem.setShippingPrice(product.getSeller().getShippingCompanies().stream().findFirst().get().getPrice());
            basketItem.setSeller(product.getSeller());
            basketItem.setShippingCompany(product.getSeller().getShippingCompanies().stream().findFirst().get());
            return basketItem;
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

        user.ifPresent(u -> {
            order.setUser(u);
            u.getOrders().add(order);
        });

        return orderRepository.save(order);
    }

    @Transactional
    public void completeOrder(String conversationId,PaymentStatus paymentStatus) {
        try {
            Order order = orderRepository.findById(UUID.fromString(conversationId))
                    .orElseThrow(() -> new NotFoundException("Order not found"));
            if(paymentStatus.equals(PaymentStatus.PAID)) {
                order.setPaymentStatus(paymentStatus);
                for(BasketItem bi: order.getBasketItems()) {
                    attributeService.decreaseStock(bi.getAttributeId(), bi.getQuantity());
                }
                orderRepository.save(order);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public Page<OrderDto> getAllOrder(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderDto::toDto);
    }

    public List<OrderDto> findOrdersByPhoneNumberOrUsername(String searchParam) {
        return orderRepository.findOrdersByPhoneNumberOrUsername(searchParam)
                .stream()
                .map(OrderDto::toDto)
                .collect(Collectors.toList());
    }

    public Page<OrderDto> findOrderByUserId(Pageable pageable) {
        String userId = authenticationFacade.getCurrentUserId();
        System.out.println(userId);
        return orderRepository.findOrderByUserId(UUID.fromString(userId), pageable)
                .map(OrderDto::toDto);
    }

    public Page<OrderDto> findOrdersContainingBasketItemsForCompany(String companyId, Pageable pageable) {
        return orderRepository.findOrdersContainingBasketItemsForCompany(UUID.fromString(companyId), pageable)
                .map(OrderDto::toDto);
    }

}
