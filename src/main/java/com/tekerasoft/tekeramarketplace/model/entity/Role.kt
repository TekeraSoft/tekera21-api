package com.tekerasoft.tekeramarketplace.model.entity

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    MODERATOR,
    DEVELOPER,
    SUPPORT_AGENT,
    FINANCE_MANAGER,
    MARKETING_MANAGER,
    AUDITOR,
    COMPANY_ADMIN,
    COMPANY_EMPLOYEE,
    CUSTOMER,
    COURIER;

    @Override
    override fun getAuthority(): String = name
}