package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.request.*;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.entity.Address;
import com.tekerasoft.tekeramarketplace.model.entity.BasketItem;
import com.tekerasoft.tekeramarketplace.model.entity.Buyer;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus;
import com.tekerasoft.tekeramarketplace.repository.jparepository.OrderRepository;
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
    public List<Order> createOrder(CreateOrderRequest req) {
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
            // seller & shipping info
            basketItem.setSeller(product.getSeller());
            basketItem.setShippingCompany(product.getSeller().getShippingCompanies().stream().findFirst().get());
            basketItem.setShippingPrice(product.getSeller().getShippingCompanies().stream().findFirst().get().getPrice());

            return basketItem;
        }).toList();

        // TODO: group basket items by seller (use seller id as key to avoid relying on Seller.equals/hashCode)
        Map<String, List<BasketItem>> itemsBySeller = basketItems.stream()
                .collect(Collectors.groupingBy(bi -> bi.getSeller().getId().toString()));

        List<Order> ordersToSave = new ArrayList<>();

        // For each seller create a separate order
        for (Map.Entry<String, List<BasketItem>> entry : itemsBySeller.entrySet()) {
            List<BasketItem> sellerItems = entry.getValue();
            // seller object (all items belong to same seller)
            Seller seller = sellerItems.get(0).getSeller();

            Order order = new Order();
            order.setBasketItems(sellerItems);

            // Buyer (new instance per order to avoid shared state issues with ORM)
            Buyer buyer = new Buyer();
            buyer.setName(req.getBuyer().getName());
            buyer.setSurname(req.getBuyer().getSurname());
            buyer.setEmail(req.getBuyer().getEmail());
            buyer.setGsmNumber(req.getBuyer().getGsmNumber());
            buyer.setIdentityNumber(req.getBuyer().getIdentityNumber());
            buyer.setRegistered(user.isPresent());
            order.setBuyer(buyer);

            // Shipping address (new instance)
            Address shippingAddress = new Address();
            shippingAddress.setCity(req.getShippingAddress().getCity());
            shippingAddress.setCountry(req.getShippingAddress().getCountry());
            shippingAddress.setPostalCode(req.getShippingAddress().getPostalCode());
            shippingAddress.setStreet(req.getShippingAddress().getStreet());
            shippingAddress.setBuildNo(req.getShippingAddress().getBuildNo());
            shippingAddress.setDoorNumber(req.getShippingAddress().getDoorNumber());
            shippingAddress.setDetailAddress(req.getShippingAddress().getDetailAddress());
            order.setShippingAddress(shippingAddress);

            // Billing address (either provided or same as shipping)
            if (req.getBillingAddress() != null) {
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

            // total price for this seller's order: sum(price * qty) + sum(shippingPrice * qty)
            // assumes BasketItem.price and BasketItem.shippingPrice are BigDecimal
            BigDecimal itemsTotal = sellerItems.stream()
                    .map(it -> it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal shippingTotal = sellerItems.stream()
                    .map(it -> it.getShippingPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            order.setTotalPrice(itemsTotal.add(shippingTotal)); // assumes Order.totalPrice is BigDecimal

            // payment fields (carried from request; adjust if payment per-seller differs)
            order.setPaymentStatus(req.getPaymentStatus());
            order.setPaymentType(req.getPaymentType());

            // attach user if present
            user.ifPresent(u -> {
                order.setUser(u);
                u.getOrders().add(order);
            });

            ordersToSave.add(order);
        }

        // persist all orders in one shot (transactional)
        return orderRepository.saveAll(ordersToSave);
    }

    @Transactional
    public void completeOrder(String conversationId, PaymentStatus paymentStatus) {
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
        return orderRepository.findOrderByUserId(UUID.fromString(userId), pageable)
                .map(OrderDto::toDto);
    }

    public Page<OrderDto> findOrdersContainingBasketItemsForCompany(String companyId, Pageable pageable) {
        return orderRepository.findOrdersContainingBasketItemsForCompany(UUID.fromString(companyId), pageable)
                .map(OrderDto::toDto);
    }

}
