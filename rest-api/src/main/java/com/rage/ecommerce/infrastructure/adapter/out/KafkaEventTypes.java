package com.rage.ecommerce.infrastructure.adapter.out;

public final class KafkaEventTypes {
    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_READY_TO_CHECK_OFFER = "order.offer.ready.to.check";
    public static final String ORDER_OFFER_CHECKED = "order.offer.checked";
    public static final String ORDER_OFFER_APPLIED = "order.offer.applied";
    public static final String ORDER_MAKE_PAYMENT = "order.make.payment";
    public static final String ORDER_MAKE_PROCESSED = "order.payment.processed";
    public static final String ORDER_PAYMENT_SUCCESSFUL = "order.payment.successful";
    public static final String ORDER_PAYMENT_FAILED = "order.payment.failed";
    public static final String ORDER_SHIPPED = "order.shipped";
    public static final String ORDER_DELIVERED = "order.delivered";
    public static final String ORDER_POSTPROCESSED = "order.postprocessed";
}
