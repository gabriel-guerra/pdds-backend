package com.pdds.controller;

import com.pdds.domain.Cart;
import com.pdds.domain.Order;
import com.pdds.domain.User;
import com.pdds.dto.SelectedProductResponseDTO;
import com.pdds.dto.MessageResponseDTO;
import com.pdds.dto.UpdateCartDTO;
import com.pdds.service.CartService;
import com.pdds.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    CartController(CartService cartService, OrderService orderService){
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<SelectedProductResponseDTO>> getAllItemFromCart(@AuthenticationPrincipal User principal){

        Optional<Cart> opt = cartService.findByUser(principal);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Cart cart = opt.get();
        return ResponseEntity.ok().body(cartService.listOfItems(cart));

    }

    @PostMapping()
    public ResponseEntity<MessageResponseDTO> removeFromCart(@RequestBody @Valid UpdateCartDTO updateCartDTO, @AuthenticationPrincipal User principal){

        Optional<Cart> opt = cartService.findByUser(principal);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Cart cart = opt.get();
        boolean update = cartService.removeFromCart(updateCartDTO.productId(), updateCartDTO.quantity(), cart);

        if (!update) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().body(new MessageResponseDTO("Shopping Cart updated"));

    }

    @GetMapping("/checkout")
    public ResponseEntity<MessageResponseDTO> createOrder(@AuthenticationPrincipal User principal){

        Optional<Cart> opt = cartService.findByUser(principal);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Order checkout = orderService.generateOrder(opt.get());
        if (checkout == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.status(200).body(new MessageResponseDTO("Order created"));
    }




}
