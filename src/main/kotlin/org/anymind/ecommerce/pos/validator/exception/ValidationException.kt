package org.anymind.ecommerce.pos.validator.exception

class ValidationException(private val paymentMethod: String, private val messageDetails: String) :
    RuntimeException(messageDetails) {
    override val message: String
        get() = "Validation failed for payment method [$paymentMethod]: $messageDetails"
}
