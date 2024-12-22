package com.github.michaelodusami.fakeazon.modules.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Category class represents a product category in the eCommerce platform.
 * Purpose:
 * - Organizes products into groups to enhance search and filtering
 * capabilities.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "categories")
public class Category {

    /**
     * The unique identifier for the category.
     * Impact: Provides a primary key for the "categories" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the category.
     * Impact: Identifies the category in listings and filters.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * A brief description of the category.
     * Impact: Provides additional context for users about the category.
     */
    @Column(length = 500)
    private String description;
}
