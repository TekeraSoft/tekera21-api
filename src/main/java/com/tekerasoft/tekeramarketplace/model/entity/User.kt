package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.Gender
import com.tekerasoft.tekeramarketplace.model.enums.Permission
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableSet<Role> = mutableSetOf(),

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_permissions", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    open var permissions: MutableSet<Permission>? = mutableSetOf(),

    @Enumerated(EnumType.STRING)
    open var gender: Gender,

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var orders: MutableList<SellerOrder>? = mutableListOf(),

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

    open var assignCount: Int? = 0,

    ): BaseEntity() , UserDetails {

    override fun getAuthorities(): MutableSet<out GrantedAuthority> = roles

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