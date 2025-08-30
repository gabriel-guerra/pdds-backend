package com.pdds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdds.config.TestConfig;
import com.pdds.domain.Cart;
import com.pdds.domain.Order;
import com.pdds.domain.Product;
import com.pdds.domain.User;
import com.pdds.dto.UpdateOrderDTO;
import com.pdds.repository.ProductRepository;
import com.pdds.security.TokenService;
import com.pdds.service.CartService;
import com.pdds.service.OrderService;
import com.pdds.service.ProductService;
import com.pdds.service.UserService;
import jakarta.servlet.Filter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OrderService orderService;

    private User mockUser;
    private Product mockProdut;
    private String mockToken;

    @BeforeEach
    public void beforeEach(){

        testConfig.cleanDatabase();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build()
        ;
        objectMapper = new ObjectMapper();

        testConfig.cleanDatabase();

        mockUser = (User) userService.findByEmail("john.doe@example.com");
        mockProdut = productService.findByRegex("PlayStation").get(0);
        mockToken = tokenService.generateToken(mockUser);

    }

    @Test
    public void getAllOrdersFromAUserTest() throws Exception{

        cartService.addToCart(mockProdut.getId(), 3, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + mockToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.not(Matchers.empty())))
        ;

    }

    @Test
    public void getOrderByIdTest() throws Exception{

        cartService.addToCart(mockProdut.getId(), 3, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        mockMvc.perform(get("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + mockToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.not(Matchers.empty())))
                .andExpect(jsonPath("$.total").value(order.getTotal()))
        ;

    }

    @Test
    public void updateOrderTest() throws Exception{

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

        String jsonRequest = objectMapper.writeValueAsString(newOrder);

        mockMvc.perform(post("/orders/" + order.getId() + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + mockToken)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.not(Matchers.empty())))
                .andExpect(jsonPath("$.message").value("Order updated"))
        ;

    }

    @Test
    public void deleteOrderTest() throws Exception{

        cartService.addToCart(mockProdut.getId(), 3, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        mockMvc.perform(delete("/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + mockToken))
                .andExpect(status().isNoContent())
        ;

    }




}
