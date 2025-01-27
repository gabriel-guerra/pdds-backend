package com.pdds.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectedProduct> shoppingCartProducts;

    private double total;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User userId;

    public Cart(List<SelectedProduct> shoppingCart, double total, User userId) {
        this.shoppingCartProducts = shoppingCart;
        this.total = total;
        this.userId = userId;
    }
}
