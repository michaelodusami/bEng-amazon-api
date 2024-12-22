package com.github.michaelodusami.fakeazon.modules.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.github.michaelodusami.fakeazon.common.enums.AmazonStatus;
import com.github.michaelodusami.fakeazon.modules.payment.enums.PaymentMethod;

/**
 * The Payment class represents payment details for an order in the eCommerce
 * platform.
 * Purpose:
 * - Tracks payment information and status for a specific order.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    /**
     * The unique identifier for the payment.
     * Impact: Provides a primary key for the "payments" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The order ID associated with the payment.
     * Impact: Links the payment to a specific order.
     */
    @Column(nullable = false)
    private Long orderId;

    /**
     * The payment method used (e.g., credit card, PayPal).
     * Impact: Identifies how the payment was made.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    /**
     * The status of the payment (e.g., pending, completed, failed).
     * Impact: Tracks the payment's current state.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AmazonStatus status;

    /**
     * The transaction ID provided by the payment gateway.
     * Impact: Provides a reference for tracking the transaction.
     */
    @Column(unique = true)
    private String transactionId;

    /**
     * The timestamp when the payment was created.
     * Impact: Tracks when the payment was initiated.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
