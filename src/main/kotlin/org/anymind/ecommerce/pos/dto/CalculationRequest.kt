package org.anymind.ecommerce.pos.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class CalculationRequest(
    val customerId: String,
    val price: BigDecimal,
    val paymentMethod: String,
    val datetime: LocalDateTime,
    val additionalItem: AdditionalItem?
)