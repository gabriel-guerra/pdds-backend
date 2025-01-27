package com.pdds.domain;

import com.pdds.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectedProduct> orderProducts;
    private double total;
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    public Order(List<SelectedProduct> orderProducts, OrderStatus status, User userId) {
        this.orderProducts = orderProducts;

        double total = 0;
        for (SelectedProduct sp : orderProducts){
            total += sp.getTotal();
        }

        this.total = total;
        this.status = status;
        this.userId = userId;
    }
}
