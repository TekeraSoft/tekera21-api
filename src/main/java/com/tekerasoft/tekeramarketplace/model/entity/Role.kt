package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import java.util.UUID

@Entity
@Table(name = "roles")
open class Role(
    @Column(unique = true, nullable = false)
    open var name: String
) : BaseEntity(),GrantedAuthority {
    override fun getAuthority(): String = name

    constructor(): this("")
}