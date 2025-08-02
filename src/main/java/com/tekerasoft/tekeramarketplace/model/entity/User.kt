package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users", uniqueConstraints = [
    UniqueConstraint(columnNames = ["email"])
])
open class User (
    open var firstName: String,
    open var lastName: String,
    open var email: String,
    open var hashedPassword: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    open var roles: MutableSet<Role>? = mutableSetOf(Role.CUSTOMER),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_permissions", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    open var permissions: MutableSet<Permission>? = mutableSetOf(),

    @Enumerated(EnumType.STRING)
    open var gender: Gender,

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var orders: MutableList<Order>? = mutableListOf(),

    open var gsmNumber: String,

    @OneToMany(fetch = FetchType.LAZY)
    open var followSellers: MutableList<Seller>? = mutableListOf(),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_topics", joinColumns = [JoinColumn(name = "user_id")])
    open var relatedTopics: MutableSet<String>? = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var address: MutableList<Address>? = mutableListOf(),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_fav_products", joinColumns = [JoinColumn(name = "user_id")])
    open var favProducts: List<String>? = null,

    open var birthDate: LocalDate? = null,
    open var lastLogin: LocalDateTime,

    ): BaseEntity() , UserDetails {

    override fun getAuthorities()= this.roles

    override fun getPassword() = this.hashedPassword

    override fun getUsername() = this.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

}