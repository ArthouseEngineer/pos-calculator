package org.anymind.ecommerce.pos.dto

import java.math.BigDecimal

data class CalculationResponse(
    val finalPrice: BigDecimal,
    val points: BigDecimal
)
