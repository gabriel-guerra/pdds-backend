package com.pdds.repository;

import com.pdds.domain.Cart;
import com.pdds.domain.Order;
import com.pdds.domain.SelectedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SelectedProductRepository extends JpaRepository<SelectedProduct, Long> {

    @Query("SELECT p FROM SelectedProduct p WHERE p.cart = :cart")
    List<SelectedProduct> findByCart(@Param("cart") Cart cart);

    @Query("SELECT p FROM SelectedProduct p WHERE p.order = :order")
    List<SelectedProduct> findByOrder(@Param("order") Order order);


//    if needed
//    @Query("SELECT p FROM SelectedProduct p LEFT JOIN FETCH p.product WHERE p.cart = :cart")
//    List<SelectedProduct> findByCart(@Param("cart") Cart cart);
//
//    @Query("SELECT p FROM SelectedProduct p LEFT JOIN FETCH p.product WHERE p.order = :order")
//    List<SelectedProduct> findByOrder(@Param("order") Order order);


}
