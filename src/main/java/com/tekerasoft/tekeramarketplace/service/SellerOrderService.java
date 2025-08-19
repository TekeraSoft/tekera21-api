package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.SellerOrderDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateOrderRequest;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SellerOrderRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellerOrderService {
    private final SellerOrderRepository sellerOrderRepository;
    private final UserService userService;
    private final AttributeService attributeService;
    private final ProductService productService;
    private final VariationService variationService;
    private final AuthenticationFacade authenticationFacade;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CartService cartService;

    public SellerOrderService(SellerOrderRepository sellerOrderRepository,
                              UserService userService,
                              AttributeService attributeService,
                              ProductService productService,
                              VariationService variationService,
                              AuthenticationFacade authenticationFacade, SimpMessagingTemplate simpMessagingTemplate, CartService cartService) {
        this.sellerOrderRepository = sellerOrderRepository;
        this.userService = userService;
        this.attributeService = attributeService;
        this.productService = productService;
        this.variationService = variationService;
        this.authenticationFacade = authenticationFacade;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.cartService = cartService;
    }


    @Transactional
    public List<SellerOrder> createSellerOrder(CreateOrderRequest req) {
        try {
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
                basketItem.setAttributeId(attribute.getId().toString());
                basketItem.setCode(product.getCode());
                basketItem.setBrandName(product.getBrandName());
                basketItem.setQuantity(bi.getQuantity());
                basketItem.setModelCode(variation.getModelCode());
                basketItem.setPrice(attribute.getPrice());
                basketItem.setSku(attribute.getSku());
                basketItem.setBarcode(attribute.getBarcode());
                basketItem.setImage(variation.getImages().get(0));
                basketItem.setAttributes(attribute.getAttributeDetails().stream()
                        .map(it -> new BasketAttributes(it.getKey(), it.getValue()))
                        .toList());
                basketItem.setSeller(product.getSeller());
                basketItem.setShippingCompany(product.getSeller().getShippingCompanies().stream().findFirst().get());
                basketItem.setShippingPrice(product.getSeller().getShippingCompanies().stream().findFirst().get().getPrice());

                return basketItem;
            }).toList();

            // Basket item'ları seller bazında grupla
            Map<String, List<BasketItem>> itemsBySeller = basketItems.stream()
                    .collect(Collectors.groupingBy(bi -> bi.getSeller().getId().toString()));

            List<SellerOrder> ordersToSave = new ArrayList<>();

            for (Map.Entry<String, List<BasketItem>> entry : itemsBySeller.entrySet()) {
                String sellerId = entry.getKey();
                List<BasketItem> sellerItems = entry.getValue();
                Seller seller = sellerItems.get(0).getSeller();

                // Önceden var olan SellerOrder'ı bul
                Optional<SellerOrder> existingOrderOpt = sellerOrderRepository.findPendingOrderBySeller(
                        seller.getId(),
                        PaymentStatus.PENDING
                );

                SellerOrder sellerOrder;
                if (existingOrderOpt.isPresent()) {
                    // Var olan order'a yeni basket item ekle
                    sellerOrder = existingOrderOpt.get();
                    sellerOrder.getBasketItems().addAll(sellerItems);
                } else {
                    // Yeni SellerOrder oluştur
                    sellerOrder = new SellerOrder();
                    sellerOrder.setBasketItems(sellerItems);

                    Buyer buyer = new Buyer();
                    buyer.setName(req.getBuyer().getName());
                    buyer.setSurname(req.getBuyer().getSurname());
                    buyer.setEmail(req.getBuyer().getEmail());
                    buyer.setGsmNumber(req.getBuyer().getGsmNumber());
                    buyer.setIdentityNumber(req.getBuyer().getIdentityNumber());
                    buyer.setRegistered(user.isPresent());
                    sellerOrder.setBuyer(buyer);

                    Address shippingAddress = new Address();
                    shippingAddress.setCity(req.getShippingAddress().getCity());
                    shippingAddress.setCountry(req.getShippingAddress().getCountry());
                    shippingAddress.setPostalCode(req.getShippingAddress().getPostalCode());
                    shippingAddress.setStreet(req.getShippingAddress().getStreet());
                    shippingAddress.setBuildNo(req.getShippingAddress().getBuildNo());
                    shippingAddress.setDoorNumber(req.getShippingAddress().getDoorNumber());
                    shippingAddress.setDetailAddress(req.getShippingAddress().getDetailAddress());
                    sellerOrder.setShippingAddress(shippingAddress);

                    if (req.getBillingAddress() != null) {
                        Address billingAddress = new Address();
                        billingAddress.setCity(req.getBillingAddress().getCity());
                        billingAddress.setCountry(req.getBillingAddress().getCountry());
                        billingAddress.setPostalCode(req.getBillingAddress().getPostalCode());
                        billingAddress.setStreet(req.getBillingAddress().getStreet());
                        billingAddress.setBuildNo(req.getBillingAddress().getBuildNo());
                        billingAddress.setDoorNumber(req.getBillingAddress().getDoorNumber());
                        billingAddress.setDetailAddress(req.getBillingAddress().getDetailAddress());
                        sellerOrder.setBillingAddress(billingAddress);
                    } else {
                        sellerOrder.setBillingAddress(shippingAddress);
                    }

                    BigDecimal itemsTotal = sellerItems.stream()
                            .map(it -> it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    sellerOrder.setShippingPrice(seller.getShippingCompanies().stream().findFirst().get().getPrice());
                    sellerOrder.setTotalPrice(itemsTotal);
                    sellerOrder.setPaymentStatus(req.getPaymentStatus());
                    sellerOrder.setPaymentType(req.getPaymentType());

                    user.ifPresent(u -> {
                        sellerOrder.setUser(u);
                        u.getOrders().add(sellerOrder);
                    });
                }

                ordersToSave.add(sellerOrder);
            }

            return sellerOrderRepository.saveAll(ordersToSave);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }


    @Transactional
    public void completeOrder(String sellerOrderId, PaymentStatus paymentStatus,String cartId) {
        try {
            SellerOrder order = sellerOrderRepository.findById(UUID.fromString(sellerOrderId))
                    .orElseThrow(() -> new NotFoundException("Order not found"));
            if (paymentStatus.equals(PaymentStatus.PAID)) {
                order.setPaymentStatus(paymentStatus);
                cartService.clearCart(cartId);

                for (BasketItem bi : order.getBasketItems()) {
                    attributeService.decreaseStock(bi.getAttributeId(), bi.getQuantity());
                }
                sellerOrderRepository.save(order);

                Set<UUID> sellerIds = order.getBasketItems().stream()
                        .map(bi -> bi.getSeller().getId())
                        .collect(Collectors.toSet());
                for(UUID sellerId : sellerIds) {
                    simpMessagingTemplate.convertAndSend(
                            "/topic/sellerOrders/"+sellerId.toString(),
                            SellerOrderDto.toDto(order));
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }


    public List<SellerOrderDto> findOrdersByPhoneNumberOrUsername(String searchParam) {
        return sellerOrderRepository.findOrdersByPhoneNumberOrUsername(searchParam)
                .stream()
                .map(SellerOrderDto::toDto)
                .collect(Collectors.toList());
    }

    public Page<SellerOrderDto> findOrderByUserId(Pageable pageable) {
        String userId = authenticationFacade.getCurrentUserId();
        return sellerOrderRepository.findOrderByUserId(UUID.fromString(userId), pageable)
                .map(SellerOrderDto::toDto);
    }

    public Page<SellerOrderDto> findSellerOrdersByUserId(Pageable pageable) {
        String userId = authenticationFacade.getCurrentUserId();
        return sellerOrderRepository.findSellerOrdersByUserId(UUID.fromString(userId), pageable)
                .map(SellerOrderDto::toDto);
    }
}
