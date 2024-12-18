package org.anymind.ecommerce.pos.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class SalesResponseDto(
    val datetime: LocalDateTime,
    val sales: BigDecimal,
    val points: BigDecimal
)
