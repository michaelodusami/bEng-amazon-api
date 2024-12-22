package com.github.michaelodusami.fakeazon.modules.cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.michaelodusami.fakeazon.modules.product.entity.Product;
import java.util.List;

/**
 * The ShoppingCart class represents a customer's shopping cart in the eCommerce
 * platform.
 * Purpose:
 * - Allows customers to temporarily store products they intend to purchase.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {

    /**
     * The unique identifier for the shopping cart.
     * Impact: Provides a primary key for the "shopping_carts" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer ID associated with the shopping cart.
     * Impact: Links the cart to a specific customer.
     */
    @Column(nullable = false)
    private Long customerId;

    /**
     * The list of products in the shopping cart.
     * Impact: Tracks the products the customer intends to purchase.
     */
    @ManyToMany
    @JoinTable(name = "cart_products", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    /**
     * The total price of all items in the cart.
     * Impact: Helps calculate the cost for checkout.
     */
    private Double totalPrice;
}
