package com.pdds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdds.config.TestConfig;
import com.pdds.domain.Cart;
import com.pdds.domain.Product;
import com.pdds.domain.User;
import com.pdds.dto.UpdateCartDTO;
import com.pdds.repository.ProductRepository;
import com.pdds.security.TokenService;
import com.pdds.service.CartService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

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

    private String tokenAdm;
    private String tokenUser;

    @BeforeEach
    public void beforeEach() {

        testConfig.cleanDatabase();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build()
        ;
        objectMapper = new ObjectMapper();

        testConfig.cleanDatabase();

        tokenAdm = testConfig.getTokenAdm();
        tokenUser = testConfig.getTokenUser();

    }


    @Test
    public void findAllItemsFromACartTest() throws Exception{

        User user = (User) userService.findByEmail("clark.kent@example.com");
        String generatedToken = tokenService.generateToken(user);
        Product product1 = productService.findByRegex("PlayStation").get(0);
        Product product2 = productService.findByRegex("iPhone").get(0);

        cartService.addToCart(product1.getId(), 5, user);
        cartService.addToCart(product2.getId(), 2, user);

        mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + generatedToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists())
        ;

    }

    @Test
    public void removeItemFromCartTest() throws Exception{

        User user = (User) userService.findByEmail("john.doe@example.com");
        Product product = productService.findByRegex("PlayStation").get(0);
        String generatedToken = tokenService.generateToken(user);

        cartService.addToCart(product.getId(), 5, user);

        UpdateCartDTO updateCartDTO = new UpdateCartDTO(product.getId(), 5);
        String jsonContent = objectMapper.writeValueAsString(updateCartDTO);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + generatedToken)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Shopping Cart updated"))
        ;

    }

    @Test
    public void failRemoveMoreUnitsThanExistsTest() throws Exception{

        User user = (User) userService.findByEmail("john.doe@example.com");
        Product product = productService.findByRegex("PlayStation").get(0);
        String generatedToken = tokenService.generateToken(user);

        UpdateCartDTO updateCartDTO = new UpdateCartDTO(product.getId(), 5);
        String jsonContent = objectMapper.writeValueAsString(updateCartDTO);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + generatedToken)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    public void checkoutTest() throws Exception{

        User user = (User) userService.findByEmail("john.doe@example.com");
        Product product = productService.findByRegex("PlayStation").get(0);
        String generatedToken = tokenService.generateToken(user);

        UpdateCartDTO updateCartDTO = new UpdateCartDTO(product.getId(), 5);
        String jsonContent = objectMapper.writeValueAsString(updateCartDTO);

        mockMvc.perform(get("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + generatedToken)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order created"))
        ;

    }



}
