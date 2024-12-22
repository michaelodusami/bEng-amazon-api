package com.github.michaelodusami.fakeazon.modules.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * The Product class represents an item available in the eCommerce platform.
 * Purpose:
 * - Provides detailed information about products for the catalog and
 * purchasing.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "products")
public class Product {

    /**
     * The unique identifier for the product.
     * Impact: Provides a primary key for the "products" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the product.
     * Impact: Identifies the product in listings and searches.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The description of the product.
     * Impact: Provides detailed information to customers about the product.
     */
    @Column(length = 1000)
    private String description;

    /**
     * The price of the product.
     * Impact: Indicates the cost to the customer.
     */
    @Column(nullable = false)
    private Double price;

    /**
     * The categories of the product.
     * Impact: Allows products to be grouped and filtered.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    /**
     * The stock quantity available for the product.
     * Impact: Determines availability for purchase.
     */
    @Column(nullable = false)
    private int stock;

    /**
     * The average rating of the product.
     * Impact: Indicates customer satisfaction.
     */
    @Builder.Default
    private Double rating = 0.0;
}
