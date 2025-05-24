package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
import java.util.UUID

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    open var id: UUID? = null

    open var createdAt: LocalDateTime = LocalDateTime.now()

    open var updatedAt: LocalDateTime = LocalDateTime.now()

}