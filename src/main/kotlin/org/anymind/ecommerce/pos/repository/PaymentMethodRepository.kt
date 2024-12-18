package org.anymind.ecommerce.pos.repository

import org.anymind.ecommerce.pos.entity.PaymentMethodEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PaymentMethodRepository : ReactiveCrudRepository<PaymentMethodEntity, Long> {
    fun findByName(name: String): Mono<PaymentMethodEntity>
}
