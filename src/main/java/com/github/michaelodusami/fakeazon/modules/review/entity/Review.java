package com.github.michaelodusami.fakeazon.modules.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.michaelodusami.fakeazon.modules.product.entity.Product;
import java.time.LocalDateTime;

/**
 * The Review class represents a product review left by a customer.
 * Purpose:
 * - Allows customers to provide feedback and ratings for products.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "reviews")
public class Review {

    /**
     * The unique identifier for the review.
     * Impact: Provides a primary key for the "reviews" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product ID associated with the review.
     * Impact: Links the review to a specific product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * The customer ID who created the review.
     * Impact: Links the review to the customer who wrote it.
     */
    @Column(nullable = false)
    private Long customerId;

    /**
     * The rating given to the product.
     * Impact: Reflects the customer's evaluation of the product.
     */
    @Max(5)
    @Min(0)
    @Column(nullable = false)
    private int rating;

    /**
     * The review text provided by the customer.
     * Impact: Gives detailed feedback about the product.
     */
    @Column(length = 1000)
    private String comment;

    /**
     * The timestamp when the review was created.
     * Impact: Tracks when the review was posted.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
