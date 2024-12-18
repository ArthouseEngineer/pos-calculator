package org.anymind.ecommerce.pos.entity

import java.math.BigDecimal
import java.time.LocalDateTime

import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("payments")
data class PaymentEntity(

    @Column("id")
    @Id val id: Long? = null,
    @Column("customer_id")
    val customerId: String,
    @Column("payment_method_id")
    val paymentMethodId: Long,
    @Column("price")
    val price: BigDecimal,
    @Column("final_price")
    val finalPrice: BigDecimal,
    @Column("points")
    val points: BigDecimal,
    @Column("additional_details")
    val additionalDetails: Json,
    @Column("datetime")
    val datetime: LocalDateTime,
    @Column("created_at")
    val createdAt: LocalDateTime,

    @Transient val paymentMethodEntity: PaymentMethodEntity? = null
)
