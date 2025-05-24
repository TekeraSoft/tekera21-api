package com.tekerasoft.tekeramarketplace.model.document

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
open class ThreeDOrder(
    @Id
    open var orderId: String,
    open var userId: String,
    open var targetPic: String,
    open var orderStatus: String,
)