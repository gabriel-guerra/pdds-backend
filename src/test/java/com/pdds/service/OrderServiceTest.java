package com.pdds.service;

import com.pdds.config.TestConfig;
import com.pdds.domain.*;
import com.pdds.domain.enums.OrderStatus;
import com.pdds.dto.UpdateOrderDTO;
import com.pdds.repository.CartRepository;
import com.pdds.repository.ProductRepository;
import com.pdds.repository.UserRepository;
import com.pdds.security.TokenService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private TokenService tokenService;

    private User mockUser;
    private Product mockProdut;
    private String mockToken;

    @BeforeEach
    public void beforeEach(){

        testConfig.cleanDatabase();

        mockUser = (User) userService.findByEmail("john.doe@example.com");
        mockProdut = productService.findByRegex("PlayStation").get(0);
        mockToken = tokenService.generateToken(mockUser);

    }

    @Test
    public void getAllOrdersFromUserTest(){

        Product nintendo = productService.findByRegex("Nintendo").get(0);
        cartService.addToCart(nintendo.getId(), 1, mockUser);
        Cart cartNintendo = cartService.findByUser(mockUser).get();
        Order orderNintendo = orderService.generateOrder(cartNintendo);

        Product xbox = productService.findByRegex("Xbox").get(0);
        cartService.addToCart(xbox.getId(), 1, mockUser);
        Cart cartXbox = cartService.findByUser(mockUser).get();
        Order orderXbox = orderService.generateOrder(cartXbox);

        List<Order> orders = orderService.findByUser(mockUser);

        Assertions.assertFalse(orders.isEmpty());
        Assertions.assertTrue(orders.size() > 1);

    }

    @Test
    public void getOrderByIdTest(){

        cartService.addToCart(mockProdut.getId(), 4, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        Optional<Order> opt = orderService.findById(order.getId());
        Assertions.assertTrue(opt.isPresent());

        Order foundOrder = opt.get();
        Assertions.assertEquals(order.getUserId().getId(), foundOrder.getUserId().getId());
        Assertions.assertEquals(order.getTotal(), foundOrder.getTotal());
        Assertions.assertEquals(order.getStatus(), foundOrder.getStatus());

    }

    @Test
    public void changeStatusTest(){

        cartService.addToCart(mockProdut.getId(), 4, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        boolean changed = orderService.changeStatus(order, OrderStatus.CONFIRMED);

        Assertions.assertTrue(changed);
        Assertions.assertEquals(OrderStatus.CONFIRMED, order.getStatus());

    }

    @Test
    public void updateOrderTest(){

        Product nintendo = productService.findByRegex("Nintendo").get(0);
        Product xbox = productService.findByRegex("Xbox").get(0);
        cartService.addToCart(mockProdut.getId(), 3, mockUser);
        cartService.addToCart(nintendo.getId(), 9, mockUser);

        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        List<UpdateOrderDTO> newOrder = new ArrayList<>();

        newOrder.add(new UpdateOrderDTO(mockProdut.getId(), 9));
        newOrder.add(new UpdateOrderDTO(nintendo.getId(), 0));
        newOrder.add(new UpdateOrderDTO(xbox.getId(), 9));

        double totalExpected = (9*mockProdut.getPrice()) + (9* xbox.getPrice());

        Order updatedOrder = orderService.update(order, newOrder);

        Assertions.assertNotNull(updatedOrder);
        Assertions.assertFalse(updatedOrder.getOrderProducts().isEmpty());
        Assertions.assertEquals(order.getOrderProducts().size(), updatedOrder.getOrderProducts().size());

        List<SelectedProduct> selectedProducts = updatedOrder.getOrderProducts();


        Assertions.assertTrue(
                selectedProducts.stream().anyMatch(
                        sp ->  sp.getProduct().getId() == mockProdut.getId() && sp.getQuantity() == 9
                )
        );

        Assertions.assertTrue(
                selectedProducts.stream().anyMatch(
                        sp -> sp.getProduct().getId() == xbox.getId() && sp.getQuantity() == 9
                )
        );

        double totalAfterUpdate = selectedProducts.stream().mapToDouble(SelectedProduct::getTotal).sum();

        Assertions.assertEquals(totalExpected, totalAfterUpdate);

    }

    @Test
    public void deleteOrderTest(){

        cartService.addToCart(mockProdut.getId(), 4, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        boolean delete = orderService.deleteById(order.getId());

        Assertions.assertTrue(delete);
        Assertions.assertTrue(orderService.findById(order.getId()).isEmpty());

    }


    @Test
    public void generateOrderTest(){

        // preparation
        int qtToAdd = 5;
        Product product = productService.findByRegex("Nintendo").get(0);
        User user = (User) userService.findByEmail("john.doe@example.com");
        cartService.addToCart(product.getId(), qtToAdd, user);
        Optional<Cart> opt = cartRepository.findByUser(user);
        Cart cart = opt.get();

        //saving values to compare later
        double totalExpected = cart.getTotal();
        int cartSizeExpected = cart.getShoppingCartProducts().size();

        Order generateOrder = orderService.generateOrder(cart);
        Assertions.assertNotNull(generateOrder);
        Assertions.assertEquals(totalExpected, generateOrder.getTotal());
        Assertions.assertEquals(cartSizeExpected, generateOrder.getOrderProducts().size());
        Assertions.assertEquals(cart.getUserId().getId(), generateOrder.getUserId().getId());

        // after cleaning shopping cart
        Assertions.assertEquals(0.0, cart.getTotal());
        Assertions.assertEquals(0, cart.getShoppingCartProducts().size());

    }


}
