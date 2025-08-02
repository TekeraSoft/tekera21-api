package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.enums.Gender
import java.time.LocalDate

data class UserDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: Gender,
    val birthDate: LocalDate,

)
