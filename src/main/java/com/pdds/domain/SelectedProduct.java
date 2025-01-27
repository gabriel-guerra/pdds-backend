package com.pdds.domain;

import com.pdds.domain.enums.SelectedProductOperation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "selected_products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SelectedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private SelectedProductOperation operation;
    private int quantity;
    private double total;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "order_ref")
    private Order order;

    public SelectedProduct(SelectedProductOperation operation, int quantity, Product product, Cart cart) {
        this.operation = operation;
        this.quantity = quantity;
        this.total = product.getPrice() * quantity;
        this.product = product;
        this.cart = cart;
    }

}
