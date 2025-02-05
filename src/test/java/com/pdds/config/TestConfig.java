package com.pdds.config;


import com.pdds.domain.*;
import com.pdds.domain.enums.OrderStatus;
import com.pdds.domain.enums.Role;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.repository.*;
import com.pdds.security.TokenService;
import com.pdds.service.CartService;
import com.pdds.service.OrderService;
import com.pdds.service.ProductService;
import com.pdds.service.SelectedProductService;
import com.pdds.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
public class TestConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private SelectedProductRepository selectedProductRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SelectedProductService selectedProductService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private final JSONArray arrUser;
    private final JSONArray arrProducts;

    TestConfig(){
        try {
            String content = new String(Files.readAllBytes(Paths.get("src\\test\\resources\\mockData.json")));
            JSONObject jsonObject = new JSONObject(content);
            this.arrUser = jsonObject.getJSONArray("users");
            this.arrProducts = jsonObject.getJSONArray("products");
        }catch (Exception e){
            throw new RuntimeException("Problem trying to open the file", e);
        }
    }

    public String getTokenAdm(){
        return tokenService.generateToken((User) userRepository.findByEmail("admin@example.com"));
    }

    public String getTokenUser(){
        return tokenService.generateToken((User) userRepository.findByEmail("user@example.com"));
    }

    public void fillUsersMockDatabase(){
        try{

            for (int i = 0; i<arrUser.length(); i++){
                JSONObject jo = (JSONObject) arrUser.get(i);
                User user = new User(
                        (String) jo.get("email"),
                        new BCryptPasswordEncoder().encode((String) jo.get("password")),
                        (String) jo.get("fullName"),
                        Utils.stringToDate((String) jo.get("birthday")),
                        Role.valueOf((String) jo.get("role"))
                );

                user = userRepository.save(user);

                Cart cart = new Cart(
                        new ArrayList<>(),
                        user
                );

                cartRepository.save(cart);
            }

        }catch (Exception e){
            throw new RuntimeException("Problem trying to open the file", e);
        }
    }

    public void fillProductsMockDatabase(){

        try{
            for (int i = 0; i<arrProducts.length(); i++){
                JSONObject jo = (JSONObject) arrProducts.get(i);
                Product product = new Product(
                        (String) jo.get("name"),
                        (double) jo.get("price"),
                        (int) jo.get("stock")
                );

                productRepository.save(product);
            }

        }catch (Exception e){
            throw new RuntimeException("Problem trying to open the file", e);
        }

    }

    public void fillCartMockDatabase(){

//        User user1 = (User) userRepository.findByEmail("john.doe@example.com");
//        User user2 = (User) userRepository.findByEmail("clark.kent@example.com");
//        User user3 = (User) userRepository.findByEmail("romeo.kling@example.com");
//
//        Product p1 = productService.findByRegex("Xbox").get(0);
//        Product p2 = productService.findByRegex("Nintendo").get(0);
//        Product p3 = productService.findByRegex("iPhone").get(0);
//
//        cartService.addToCart(p1.getId(), 5, user1);
//        cartService.addToCart(p2.getId(), 3, user1);
//        cartService.addToCart(p3.getId(), 5, user2);
//        cartService.addToCart(p1.getId(), 5, user3);


    }



    public void cleanDatabase(){
        selectedProductRepository.deleteAll();
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        fillUsersMockDatabase();
        fillProductsMockDatabase();
        fillCartMockDatabase();
    }


}
