package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Role
import com.tekerasoft.tekeramarketplace.model.entity.User
import com.tekerasoft.tekeramarketplace.model.enums.Gender
import java.time.LocalDateTime

data class UserAdminDto(
    val id: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val gender: Gender,
    val lastLoginDate: LocalDateTime?,
    val roles: MutableSet<Role>?,
) {
    companion object {
        @JvmStatic
        fun toDto(from: User): UserAdminDto {
            return UserAdminDto(
                from.id.toString(),
                from.email,
                from.firstName,
                from.lastName,
                from.gender,
                from.lastLogin,
                from.roles
            )
        }
    }
}
