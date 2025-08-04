package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.Gender
import com.tekerasoft.tekeramarketplace.model.enums.Permission
import com.tekerasoft.tekeramarketplace.model.enums.Role
import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users", uniqueConstraints = [
    UniqueConstraint(columnNames = ["email"])
])
data class User (
    var firstName: String,
    var lastName: String,
    var email: String,
    var hashedPassword: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<Role>? = mutableSetOf(Role.CUSTOMER),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_permissions", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var permissions: MutableSet<Permission>? = mutableSetOf(),

    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var orders: MutableList<Order>? = mutableListOf(),

    var gsmNumber: String,

    @OneToMany(fetch = FetchType.LAZY)
    var followSellers: MutableList<Seller>? = mutableListOf(),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_topics", joinColumns = [JoinColumn(name = "user_id")])
    var relatedTopics: MutableSet<String>? = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var address: MutableList<Address>? = mutableListOf(),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_fav_products", joinColumns = [JoinColumn(name = "user_id")])
    var favProducts: List<String>? = null,

    var birthDate: LocalDate? = null,
    var lastLogin: LocalDateTime,

    var assignCount: Int? = 0,

    ): BaseEntity() , UserDetails {

    override fun getAuthorities()= this.roles

    override fun getPassword() = this.hashedPassword

    override fun getUsername() = this.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

    constructor() : this(
        "",
        "",
        "",
        "",
        mutableSetOf(),
        mutableSetOf(),
        Gender.FEMALE,
        mutableListOf(),
        "",
        mutableListOf(),
        mutableSetOf(),
        mutableListOf(),
        listOf(),
        null,
        LocalDateTime.now(),
        0)

}