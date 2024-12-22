package com.github.michaelodusami.fakeazon.modules.address.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Address class represents a customer's address in the eCommerce platform.
 * Purpose:
 * - Stores billing and shipping address details for customers.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "addresses")
public class Address {

    /**
     * The unique identifier for the address.
     * Impact: Provides a primary key for the "addresses" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer ID associated with the address.
     * Impact: Links the address to a specific customer.
     */
    @Column(nullable = false)
    private Long customerId;

    /**
     * The type of address (e.g., shipping, billing).
     * Impact: Differentiates between address purposes.
     */
    @Column(nullable = false)
    private String type;

    /**
     * The first line of the address.
     * Impact: Identifies the primary part of the address.
     */
    @Column(nullable = false)
    private String line1;

    /**
     * The second line of the address (optional).
     * Impact: Provides additional address details.
     */
    private String line2;

    /**
     * The city for the address.
     * Impact: Specifies the city for delivery or billing.
     */
    @Column(nullable = false)
    private String city;

    /**
     * The state or region for the address.
     * Impact: Provides regional details for the address.
     */
    @Column(nullable = false)
    private String state;

    /**
     * The postal code for the address.
     * Impact: Ensures accurate delivery or billing location.
     */
    @Column(nullable = false)
    private String postalCode;

    /**
     * The country for the address.
     * Impact: Specifies the country of the address.
     */
    @Column(nullable = false)
    private String country;
}
