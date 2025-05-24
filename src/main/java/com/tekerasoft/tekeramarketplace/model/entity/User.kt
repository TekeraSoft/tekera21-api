package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    open var roles: MutableSet<Role>,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_permissions", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    open var permissions: MutableSet<Permission>,

    @Enumerated(EnumType.STRING)
    open var gender: Gender,

    @OneToOne(fetch = FetchType.LAZY)
    open var company: Company? = null,

    open var phoneNumber: String,
    open var address: String,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_fav_products", joinColumns = [JoinColumn(name = "user_id")])
    open var favProducts: List<String>? = null,

    open var birthDate: LocalDate? = null,
    open var lastLogin: LocalDateTime,
    open var isActive: Boolean

): BaseEntity()
//    : UserDetails {
//
//    override fun getAuthorities()= this.roles
//
//    override fun getPassword() = this.hashedPassword
//
//    override fun getUsername() = this.email
//
//    override fun isAccountNonExpired() = true
//
//    override fun isAccountNonLocked() = true
//
//    override fun isCredentialsNonExpired() = true
//
//    override fun isEnabled() = true
//}