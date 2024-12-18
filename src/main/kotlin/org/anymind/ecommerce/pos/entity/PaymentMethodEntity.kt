package org.anymind.ecommerce.pos.entity

import com.fasterxml.jackson.databind.JsonNode
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("payment_methods")
data class PaymentMethodEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("validation_rules")
    val validationRules: Json,
    @Column("price_modifier")
    val priceModifier: BigDecimal,
    @Column("points_modifier")
    val pointsModifier: BigDecimal,
    @Column("active")
    val active: Boolean,
    @Column("created_at")
    val createdAt: LocalDateTime,
    @Column("updated_at")
    val updatedAt: LocalDateTime
)