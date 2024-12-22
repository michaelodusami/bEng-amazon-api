package com.github.michaelodusami.fakeazon.modules.wishlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.michaelodusami.fakeazon.modules.product.entity.Product;
import java.util.List;

/**
 * The Wishlist class represents a customer's wishlist in the eCommerce
 * platform.
 * Purpose:
 * - Allows customers to save products for future consideration.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "wishlists")
public class Wishlist {

    /**
     * The unique identifier for the wishlist.
     * Impact: Provides a primary key for the "wishlists" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer ID associated with the wishlist.
     * Impact: Links the wishlist to a specific customer.
     */
    @Column(nullable = false)
    private Long customerId;

    /**
     * The list of products in the wishlist.
     * Impact: Tracks the products the customer is interested in purchasing later.
     */
    @ManyToMany
    @JoinTable(name = "wishlist_products", joinColumns = @JoinColumn(name = "wishlist_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;
}
