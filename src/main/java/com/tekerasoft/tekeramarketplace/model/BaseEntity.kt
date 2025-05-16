package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator
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