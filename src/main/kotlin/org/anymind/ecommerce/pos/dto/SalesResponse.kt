package org.anymind.ecommerce.pos.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class SalesResponse(
    val paymentMethod: String,
    val datetime: LocalDateTime,
    val sales: BigDecimal,
    val points: BigDecimal
)
