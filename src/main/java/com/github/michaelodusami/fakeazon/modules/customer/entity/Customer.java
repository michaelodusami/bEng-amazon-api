package com.github.michaelodusami.fakeazon.modules.customer.entity;

import java.util.Set;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.michaelodusami.fakeazon.modules.address.entity.Address;

/**
 * The Customer class represents the customer details associated with a user
 * account.
 * Purpose:
 * - Links customer-specific information to a user ID from the User entity.
 * - Provides additional customer data for the application, such as addresses
 * and preferences.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "customers")
public class Customer {

    /**
     * The unique identifier for the customer.
     * Impact: Provides a primary key for the "customers" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user ID associated with the customer.
     * Impact: Establishes a relationship with the User entity.
     */
    @Column(nullable = false, unique = true)
    private Long userId;

    /**
     * The customer's phone number.
     * Impact: Enables contact and verification purposes.
     */
    private String phoneNumber;

    /**
     * The customer's loyalty points.
     * Impact: Supports a rewards system.
     */
    @Column(nullable = false)
    @Builder.Default
    private int loyaltyPoints = 0;

    /**
     * The customer's primary shipping address.
     * Impact: Supports order deliveries.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_address_id")
    private Address primaryAddress;

    /**
     * Additional addresses associated with the customer.
     * Impact: Allows customers to have multiple shipping or billing addresses.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customerId")
    private List<Address> additionalAddresses;
}
