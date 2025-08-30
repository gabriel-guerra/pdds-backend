package com.pdds.controller;

import com.pdds.domain.Cart;
import com.pdds.domain.Product;
import com.pdds.domain.SelectedProduct;
import com.pdds.domain.User;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.dto.MessageResponseDTO;
import com.pdds.dto.ProductDTO;
import com.pdds.service.CartService;
import com.pdds.service.ProductService;
import com.pdds.service.SelectedProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    ProductController(ProductService productService, CartService cartService){
        this.productService = productService;
        this.cartService = cartService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponseDTO> create(@RequestBody @Valid ProductDTO productDTO){

        boolean create = productService.create(productDTO);

        if (!create) return ResponseEntity.badRequest().body(new MessageResponseDTO("Error creating the product"));

        return ResponseEntity.status(201).body(new MessageResponseDTO("Product created successfully"));

    }

//    @GetMapping()
//    public ResponseEntity<List<Product>> getAll(){
//        List<Product> products = productService.getAll();
//        return ResponseEntity.ok().body(products);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable @Valid Long id){
        Optional<Product> opt = productService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(opt.get());
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getRegex(@RequestParam(required = false) String name){

        List<Product> products;

        if (name == null) products = productService.getAll();
        else products = productService.findByRegex(name);

        return ResponseEntity.ok().body(products);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<MessageResponseDTO> update(@PathVariable @Valid Long id, @RequestBody @Valid ProductDTO productDTO){

        boolean update = productService.update(id, productDTO);

        if (!update){
            return ResponseEntity.badRequest().body(new MessageResponseDTO("There was a problem updating the product"));
        }else{
            return ResponseEntity.ok().body(new MessageResponseDTO("Product updated successfully"));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable @Valid Long id){

        boolean delete = productService.delete(id);

        if (!delete) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.status(204).build();
        }

    }

    @GetMapping("/{productId}/addCart")
    public ResponseEntity<MessageResponseDTO> addToCart(@PathVariable @Valid Long productId, @RequestParam @Valid int qt, @AuthenticationPrincipal User principal){

        boolean added = cartService.addToCart(productId, qt, principal);
        if (!added) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(new MessageResponseDTO("Product added to the Shopping Cart"));

    }

}
