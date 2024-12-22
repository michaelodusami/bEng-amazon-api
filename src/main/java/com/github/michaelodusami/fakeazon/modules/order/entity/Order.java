package com.github.michaelodusami.fakeazon.modules.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.github.michaelodusami.fakeazon.common.enums.AmazonStatus;
import com.github.michaelodusami.fakeazon.modules.product.entity.Product;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The Order class represents a completed purchase in the eCommerce platform.
 * Purpose:
 * - Tracks the details of an order placed by a customer.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "orders")
public class Order {

    /**
     * The unique identifier for the order.
     * Impact: Provides a primary key for the "orders" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer ID associated with the order.
     * Impact: Links the order to a specific customer.
     */
    @Column(nullable = false)
    private Long customerId;

    /**
     * The list of products in the order.
     * Impact: Tracks the products purchased in this order.
     */
    @ManyToMany
    @JoinTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    /**
     * The total price of the order.
     * Impact: Summarizes the cost of all items purchased.
     */
    @Column(nullable = false)
    private Double totalPrice;

    /**
     * The status of the order (e.g., pending, shipped, delivered).
     * Impact: Tracks the progress of the order.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AmazonStatus status;

    /**
     * The timestamp when the order was created.
     * Impact: Provides an audit trail for when the order was placed.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * The estimated or actual arrival date of the order.
     * Impact: Provides customers with an expected delivery timeframe.
     */
    private LocalDateTime arrivalDate;
}
