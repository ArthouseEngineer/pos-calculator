package org.anymind.ecommerce.pos.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class SalesAggregate(
    val paymentMethod: String,
    val datetime: LocalDateTime,
    val totalSales: BigDecimal,
    val totalPoints: BigDecimal
)