package org.anymind.ecommerce.pos.dto

data class AdditionalItem(
    val last4: String? = null,
    val courier: String? = null,
    val bankDetails: BankDetails? = null
)

