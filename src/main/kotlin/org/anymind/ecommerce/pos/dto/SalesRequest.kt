package org.anymind.ecommerce.pos.dto

import java.time.LocalDateTime

data class SalesRequestDto(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)