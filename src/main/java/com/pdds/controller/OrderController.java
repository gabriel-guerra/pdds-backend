package com.pdds.controller;

import com.pdds.domain.Order;
import com.pdds.domain.User;
import com.pdds.dto.MessageResponseDTO;
import com.pdds.dto.OrderResponseDTO;
import com.pdds.dto.UpdateOrderDTO;
import com.pdds.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAll(@AuthenticationPrincipal User princial){
        List<Order> orders = orderService.findByUser(princial);
        List<OrderResponseDTO> respondeList = new ArrayList<>();

        for (Order order : orders){
            respondeList.add(new OrderResponseDTO(
                    order.getId(),
                    orderService.listOfItems(order),
                    order.getTotal(),
                    order.getStatus())
            );
        }

        return ResponseEntity.ok().body(respondeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable @Valid Long id){
        Optional<Order> opt = orderService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.badRequest().build();

        Order order = opt.get();

        OrderResponseDTO responseDTO = new OrderResponseDTO(
                order.getId(),
                orderService.listOfItems(order),
                order.getTotal(),
                order.getStatus()
        );

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<MessageResponseDTO> updateOrder(@PathVariable @Valid Long id, @RequestBody List<UpdateOrderDTO> products){

        Optional<Order> opt = orderService.findById(id);
        if(opt.isEmpty()) return ResponseEntity.badRequest().build();

        orderService.update(opt.get(), products);
        return ResponseEntity.ok().body(new MessageResponseDTO("Order updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteOrder(@PathVariable @Valid Long id){

        boolean deleted = orderService.deleteById(id);
        if(!deleted) return ResponseEntity.badRequest().build();
        return ResponseEntity.noContent().build();

    }

}
