package org.anymind.ecommerce.pos.repository

import org.anymind.ecommerce.pos.dto.SalesAggregate
import org.anymind.ecommerce.pos.entity.PaymentEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface PaymentRepository : ReactiveCrudRepository<PaymentEntity, Long> {
    @Query("""
        SELECT 
            DATE_TRUNC('hour', p.datetime) AS datetime,
            pm.name AS payment_method,
            SUM(p.final_price) AS total_sales,
            SUM(p.points) AS total_points
        FROM payments p
        JOIN payment_methods pm ON p.payment_method_id = pm.id
        WHERE p.datetime BETWEEN :startDateTime AND :endDateTime
        GROUP BY datetime, pm.name
        ORDER BY datetime, pm.name;
    """)
    fun findSalesByHourAndPaymentMethod(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flux<SalesAggregate>
}
