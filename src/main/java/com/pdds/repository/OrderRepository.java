package com.pdds.repository;

import com.pdds.domain.Cart;
import com.pdds.domain.Order;
import com.pdds.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.userId = :userId")
    List<Order> findByUser(@Param("userId") User userId);

//    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProducts WHERE o.userId = :userId")
//    List<Order> findByUser(@Param("userId") User userId);


}
