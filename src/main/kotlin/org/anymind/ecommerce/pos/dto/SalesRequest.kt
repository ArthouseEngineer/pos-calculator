package org.anymind.ecommerce.pos.dto

import java.time.LocalDateTime

data class SalesRequest(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)