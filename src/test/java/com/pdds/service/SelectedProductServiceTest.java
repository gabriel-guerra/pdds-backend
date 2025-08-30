package com.pdds.service;

import com.pdds.config.TestConfig;
import com.pdds.domain.*;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.repository.SelectedProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    private User mockUser;
    private Product mockProdut;
    private String mockToken;

    @BeforeEach
    public void beforeEach(){
        email = "john.doe@example.com";

        testConfig.cleanDatabase();

        mockUser = (User) userService.findByEmail("john.doe@example.com");
        mockProdut = productService.findByRegex("PlayStation").get(0);

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

        SelectedProduct selectedProduct = selectedProductService.create(
                new SelectedProduct(SelectedProductOperation.CART, 5, product, cart)
        );

        Assertions.assertNotNull(selectedProduct);
        Assertions.assertEquals(5, selectedProduct.getQuantity());
        Assertions.assertEquals(product.getId(), selectedProduct.getProduct().getId());
    }

    @Test
    public void findAllSelectedProducts(){
        cartService.addToCart(mockProdut.getId(), 4, mockUser);
        List<SelectedProduct> products = selectedProductService.getAll();
        Assertions.assertFalse(products.isEmpty());
    }

    @Test
    public void findSelectedProductsWithCartById() throws Exception{

        cartService.addToCart(mockProdut.getId(), 2, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();

        List<SelectedProduct> products = selectedProductService.findByCart(cart);

        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals(mockProdut.getId(), products.get(0).getProduct().getId());
        Assertions.assertEquals(2, products.get(0).getQuantity());

    }

    @Test
    public void findSelectedProductsByOrder() throws Exception{

        cartService.addToCart(mockProdut.getId(), 1, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();
        Order order = orderService.generateOrder(cart);

        List<SelectedProduct> products = selectedProductService.findByOrder(order);

        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals(mockProdut.getId(), products.get(0).getProduct().getId());
        Assertions.assertEquals(1, products.get(0).getQuantity());

    }

    @Test
    public void updateSelectedProduct() throws Exception{

        cartService.addToCart(mockProdut.getId(), 1, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();

       SelectedProduct productToUpdate = selectedProductService.findByCart(cart).get(0);

       productToUpdate.setQuantity(5);
       productToUpdate.setTotal(5 * productToUpdate.getProduct().getPrice());
       selectedProductService.update(productToUpdate);

        SelectedProduct productUpdated = selectedProductService.findByCart(cart).get(0);

       Assertions.assertEquals(5, productUpdated.getQuantity());
       Assertions.assertEquals(5*productToUpdate.getProduct().getPrice(), productUpdated.getTotal());

    }


    @Test
    public void updateQuantitySelectedProduct() throws Exception{

        cartService.addToCart(mockProdut.getId(), 1, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();

        SelectedProduct productToUpdate = selectedProductService.findByCart(cart).get(0);
        selectedProductService.updateQuantity(productToUpdate, 10);

        SelectedProduct productUpdated = selectedProductService.findByCart(cart).get(0);
        Assertions.assertEquals(10, productUpdated.getQuantity());
        Assertions.assertEquals(10*productToUpdate.getProduct().getPrice(), productUpdated.getTotal());

    }

    @Test
    public void deleteSelectedProduct() throws Exception{

        cartService.addToCart(mockProdut.getId(), 1, mockUser);
        Cart cart = cartService.findByUser(mockUser).get();

        SelectedProduct productToDelete = selectedProductService.findByCart(cart).get(0);
        selectedProductService.delete(productToDelete);

        List<SelectedProduct> productsAfterDelete = selectedProductService.findByCart(cart);
        Assertions.assertTrue(productsAfterDelete.isEmpty());
        Assertions.assertTrue(selectedProductService.findById(productToDelete.getId()).isEmpty());

    }






}
