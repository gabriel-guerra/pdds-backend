package com.pdds.service;

import com.pdds.config.TestConfig;
import com.pdds.domain.Cart;
import com.pdds.domain.Order;
import com.pdds.domain.Product;
import com.pdds.domain.User;
import com.pdds.repository.CartRepository;
import com.pdds.repository.ProductRepository;
import com.pdds.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void beforeEach(){

        testConfig.cleanDatabase();

    }


    @Test
    public void generateOrderTest(){

        int qtToAdd = 5;
        Product product = productService.findByRegex("Nintendo").get(0);
        User user = (User) userService.findByEmail("john.doe@example.com");
        cartService.addToCart(product.getId(), qtToAdd, user);
        Optional<Cart> opt = cartRepository.findByUser(user);
        Cart cart = opt.get();

        boolean generateOrder = orderService.generateOrder(cart);

        Assertions.assertTrue(generateOrder);

        Order order = orderService.findByUser(user).get(0);

        Assertions.assertNotNull(order);
        Assertions.assertEquals(cart.getTotal(), order.getTotal());
        Assertions.assertEquals(cart.getShoppingCartProducts().size(), order.getOrderProducts().size());
        Assertions.assertEquals(cart.getUserId().getId(), order.getUserId().getId());

    }


}
