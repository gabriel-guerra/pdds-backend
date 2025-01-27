package com.pdds.repository;

import com.pdds.domain.Cart;
import com.pdds.domain.Order;
import com.pdds.domain.SelectedProduct;
import com.pdds.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.shoppingCartProducts WHERE c.userId = :userId")
    Optional<Cart> findByUser(@Param("userId") User userId);

}
