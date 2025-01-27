package com.pdds.service;

import com.pdds.config.TestConfig;
import com.pdds.domain.*;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.repository.SelectedProductRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class SelectedProductServiceTest {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private ProductService productService;

    @Autowired
    private SelectedProductService selectedProductService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SelectedProductRepository selectedProductRepository;

    private String email;

    @BeforeEach
    public void beforeEach(){
        email = "john.doe@example.com";

        testConfig.cleanDatabase();
    }

    public void compare(SelectedProduct s1, SelectedProduct s2, boolean compareOrder){
        Assertions.assertEquals(s1.getOperation(), s2.getOperation());
        Assertions.assertEquals(s1.getQuantity(), s2.getQuantity());
        Assertions.assertEquals(s1.getTotal(), s2.getTotal());
        Assertions.assertEquals(s1.getProduct().getId(), s2.getProduct().getId());
        Assertions.assertEquals(s1.getCart().getId(), s2.getCart().getId());
        if (compareOrder) Assertions.assertEquals(s1.getOrder().getId(), s2.getOrder().getId());
    }


    @Test
    public void createSelectedProductTest(){

        User user = (User) userService.findByEmail("john.doe@example.com");
        Cart cart = cartService.findByUser(user).get();
        Product product = productService.findByRegex("Xbox").get(0);

        SelectedProduct selectedProduct = new SelectedProduct(SelectedProductOperation.CART, 5, product, cart);

        Assertions.assertNotNull(selectedProduct);
    }

    @Test
    public void findAllSelectedProducts(){

//        List<SelectedProduct> products = selectedProductService.getAll();
//        Assertions.assertNotNull(products);
//        Assertions.assertFalse(products.isEmpty());

    }

    @Test
    public void findSelectedProductWithCartById() throws Exception{

//        SelectedProduct selectedProduct = selectedProductService.getAll().get(1);
//        SelectedProduct foundSelectedProduct = selectedProductService.findById(selectedProduct.getId());
//        compare(selectedProduct, foundSelectedProduct, false);

    }



//    @Test
//    public void findSelectedProductWithOrderById() throws Exception{
//
//
//    }

    @Test
    public void findSelectedProductsWithCartByCart() throws Exception{



    }

    @Test
    public void findSelectedProductsWithOrderByOrder() throws Exception{

    }


}
