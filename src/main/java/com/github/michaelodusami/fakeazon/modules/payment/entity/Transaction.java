package com.github.michaelodusami.fakeazon.modules.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.github.michaelodusami.fakeazon.common.enums.AmazonStatus;

/**
 * The Transaction class represents details of a financial transaction in the
 * eCommerce platform.
 * Purpose:
 * - Tracks information related to a specific financial transaction.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    /**
     * The unique identifier for the transaction.
     * Impact: Provides a primary key for the "transactions" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The transaction ID provided by the payment gateway.
     * Impact: Provides a unique reference for the transaction.
     */
    @Column(nullable = false, unique = true)
    private String transactionId;

    /**
     * The payment ID associated with the transaction.
     * Impact: Links the transaction to a specific payment.
     */
    @Column(nullable = false)
    private Long paymentId;

    /**
     * The amount processed in the transaction.
     * Impact: Indicates the financial value of the transaction.
     */
    @Column(nullable = false)
    private Double amount;

    /**
     * The status of the transaction (e.g., successful, failed, pending).
     * Impact: Tracks the outcome of the transaction.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AmazonStatus status;

    /**
     * The timestamp when the transaction was created.
     * Impact: Tracks when the transaction occurred.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
