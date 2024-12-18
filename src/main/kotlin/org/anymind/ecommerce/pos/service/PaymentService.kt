package org.anymind.ecommerce.pos.service

import org.anymind.ecommerce.pos.entity.PaymentEntity
import org.anymind.ecommerce.pos.repository.PaymentRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {

    fun savePayment(paymentEntity: PaymentEntity): Mono<PaymentEntity> {
        return paymentRepository.save(paymentEntity)
    }
}