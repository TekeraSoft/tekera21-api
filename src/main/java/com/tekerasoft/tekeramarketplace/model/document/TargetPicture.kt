package com.tekerasoft.tekeramarketplace.model.document

import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
@Table(name = "target_pictures")
open class TargetPicture(
    @Id
    open var id: String? = null,
    open var targetPic: String = "",
    open var mindPath: String? = null,
    open var createdAt: LocalDateTime = LocalDateTime.now(),
)