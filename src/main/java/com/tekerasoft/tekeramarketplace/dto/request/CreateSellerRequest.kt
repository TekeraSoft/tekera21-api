package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.BankAccount
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.checkerframework.checker.units.qual.min
import java.time.LocalDateTime

data class CreateSellerRequest(
    @JsonProperty("name")
    @field:NotBlank(message = "Firma adı zorunlu !")
    val name: String,

    @JsonProperty("categoryId")
    @field:NotNull(message = "En az bir kategori ekleyiniz !")
    @field:Size(min = 1, message = "En az bir kategori ekleyiniz !")
    val categoryId: List<String>,

    @JsonProperty("email")
    @field:NotBlank(message = "Mail adresi zorunlu !")
    val email: String,

    @JsonProperty("gsmNumber")
    @field:NotBlank(message = "Gsm numara zorunlu !")
    val gsmNumber: String,

    @JsonProperty("alternativePhoneNumber")
    val alternativePhoneNumber: String,

    @JsonProperty("supportPhoneNumber")
    val supportPhoneNumber: String,

    @JsonProperty("taxNumber")
    @field:NotBlank(message = "Vergi numarası zorunlu alan !")
    val taxNumber: String,

    @JsonProperty("taxOffice")
    @field:NotBlank(message = "Vergi dairesi seçimi zorunlu !")
    val taxOffice: String,

    @JsonProperty("merisNumber")
    @field:NotBlank(message = "Mersis numarası zorunludur")
    @field:Pattern(
        regexp = "\\d{16}",
        message = "Mersis numarası 16 haneli rakamlardan oluşmalıdır"
    )
    val merisNumber: String,

    @JsonProperty("registrationDate")
    val registrationDate: LocalDateTime,

    @JsonProperty("contactPersonNumber")
    val contactPersonNumber: String,

    @JsonProperty("contactPersonTitle")
    val contactPersonTitle: String,

    @JsonProperty("address")
    @field:NotNull(message = "En az bir adres ekleyiniz !")
    @field:Size(min = 1, message = "En az bir adres ekleyiniz !")
    val address: List<Address>,

    @JsonProperty("bankAccount")
    @field:NotNull(message = "En az bir banka hesabı ekleyiniz !")
    @field:Size(min = 1, message = "En az bir banka hesabı ekleyiniz !")
    val bankAccount: List<BankAccount>,

    @JsonProperty("shippingCompanies")
    val shippingCompanies: List<String>
)
