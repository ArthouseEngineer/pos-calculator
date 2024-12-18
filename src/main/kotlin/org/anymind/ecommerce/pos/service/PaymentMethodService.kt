package org.anymind.ecommerce.pos.service

import io.r2dbc.postgresql.codec.Json
import org.anymind.ecommerce.pos.entity.PaymentMethodEntity
import org.anymind.ecommerce.pos.repository.PaymentMethodRepository
import org.anymind.ecommerce.pos.validator.exception.ValidationException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentMethodService(private val paymentMethodRepository: PaymentMethodRepository) {

    fun findByName(name: String): Mono<PaymentMethodEntity> {
        return paymentMethodRepository.findByName(name)
            .switchIfEmpty(Mono.error(ValidationException(name, "Payment method not found.")))
    }

    fun getValidationRules(name: String): Mono<Json> {
        return findByName(name)
            .map { it.validationRules }
            .switchIfEmpty(Mono.error(ValidationException(name, "Validation rules not found.")))
    }

}

