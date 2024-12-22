package com.github.michaelodusami.fakeazon.common.enums;

public enum AmazonStatus {

    // General Statuses
    PENDING,
    ACTIVE,
    INACTIVE,
    COMPLETED,
    FAILED,
    CANCELLED,

    // User/Customer Related Statuses
    USER_REGISTERED,
    USER_VERIFIED,
    USER_SUSPENDED,
    CUSTOMER_CREATED,
    CUSTOMER_UPDATED,
    CUSTOMER_DELETED,

    // Order Related Statuses
    ORDER_PLACED,
    ORDER_CONFIRMED,
    ORDER_CANCELLED,
    ORDER_SHIPPED,
    ORDER_DELIVERED,
    ORDER_RETURNED,
    ORDER_REFUNDED,

    // Payment Related Statuses
    PAYMENT_INITIATED,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    PAYMENT_REFUNDED,

    // Inventory Related Statuses
    INVENTORY_UPDATED,
    INVENTORY_LOW,
    INVENTORY_OUT_OF_STOCK,
    INVENTORY_REPLENISHED,

    // Shipping/Delivery Related Statuses
    SHIPMENT_PREPARED,
    SHIPMENT_IN_TRANSIT,
    SHIPMENT_DELIVERED,
    SHIPMENT_DELAYED,

    // Notification Related Statuses
    NOTIFICATION_SENT,
    NOTIFICATION_FAILED,

    // Miscellaneous Statuses
    REVIEW_SUBMITTED,
    REVIEW_APPROVED,
    REVIEW_REJECTED

}
