package com.tekerasoft.tekeramarketplace.model.enums

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    AUDITOR,
    FINANCE_MANAGER,
    MARKETING_MANAGER,
    MODERATOR,
    DEVELOPER,

    WITHOUT_APPROVAL_SELLER,
    SELLER,
    SELLER_EMPLOYEE,
    SELLER_SUPPORT,
    SELLER_MARKETING_MANAGER,
    SELLER_FINANCE_MANAGER,
    CUSTOMER,
    COURIER;

    @Override
    override fun getAuthority(): String = name
}