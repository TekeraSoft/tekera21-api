package com.tekerasoft.tekeramarketplace.model.document

import com.tekerasoft.tekeramarketplace.model.entity.DigitalFashionOrderType
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
open class DigitalFashionOrder (
    @Id
    open var orderId: String? = null,
    open var userId: String = "",
    open var productId: String = "",
    open var orderType: DigitalFashionOrderType? = null,
    open var fabricType: String? = null,
    open var importName: String? = null
)