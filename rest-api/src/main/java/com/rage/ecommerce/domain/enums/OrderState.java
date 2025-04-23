package com.rage.ecommerce.domain.enums;

public enum OrderState {

    CREATED,
    OFFER_CHECKING,
    OFFER_APPLIED,
    OFFER_NOT_APPLIED,
    PAYMENT_PENDING,
    PAYMENT_APPROVED,
    PAYMENT_REJECTED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED

}
