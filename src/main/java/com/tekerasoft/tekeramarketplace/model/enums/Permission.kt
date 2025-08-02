package com.tekerasoft.tekeramarketplace.model.enums

enum class Permission {
    // Genel (Ortak) Yetkiler
    PROFILE_VIEW,
    PROFILE_EDIT,
    ADDRESS_MANAGE,

    // Ürün İşlemleri (Genelde Seller & Admin)
    PRODUCT_CREATE,
    PRODUCT_EDIT,
    PRODUCT_DELETE,
    PRODUCT_VIEW,

    // Sipariş İşlemleri
    ORDER_CREATE,
    ORDER_VIEW,
    ORDER_CANCEL,
    ORDER_MANAGE, // Admin/Seller için

    // Kullanıcı & Rol Yönetimi (Admin)
    USER_MANAGE,
    ROLE_MANAGE,

    // Yorum ve Değerlendirme
    REVIEW_CREATE,
    REVIEW_EDIT,
    REVIEW_DELETE,

    // Sepet İşlemleri (Customer)
    CART_VIEW,
    CART_ADD,
    CART_REMOVE,
    CART_UPDATE,

    // Ödeme & Fatura
    PAYMENT_PROCESS,
    INVOICE_VIEW,

    // Destek & Bildirim
    SUPPORT_TICKET_CREATE,
    SUPPORT_TICKET_VIEW,
    NOTIFICATION_VIEW,

    // Satıcı Paneli Yetkileri
    SELLER_DASHBOARD_VIEW,
    SELLER_REPORT_VIEW,

    // Admin Paneli
    ADMIN_PANEL_ACCESS
}