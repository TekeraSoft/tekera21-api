package com.tekerasoft.tekeramarketplace.dto.request

import com.tekerasoft.tekeramarketplace.model.entity.Gender
import java.time.LocalDate

data class CreateUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val gsmNumber: String,
    val password: String,
    val gender: Gender,
    val birthDate: LocalDate,
)
