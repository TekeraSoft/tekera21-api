package com.tekerasoft.tekeramarketplace.builder;

import com.tekerasoft.tekeramarketplace.service.SettingService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentBuilder {
    private final SettingService settingService;

    public PaymentBuilder(SettingService settingService) {
        this.settingService = settingService;
    }

    public BigDecimal calculateShippingFee(BigDecimal total) {
        return total.compareTo(settingService.getSettings().getMinShippingPrice()) < 0 ? total.add(settingService.getSettings().getShippingPrice()) : total;
    }

    public BigDecimal selectDiscountOrPrice(BigDecimal price, BigDecimal discountPrice) {
        return discountPrice.compareTo(BigDecimal.ZERO) > 0 ? discountPrice : price;
    }

}
