package com.tekerasoft.tekeramarketplace.model.enums

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    MODERATOR,
    DEVELOPER,
    SELLER,
    SELLER_EMPLOYEE,
    SUPPORT_AGENT,
    FINANCE_MANAGER,
    MARKETING_MANAGER,
    AUDITOR,
    CUSTOMER,
    COURIER;

    @Override
    override fun getAuthority(): String = name
}