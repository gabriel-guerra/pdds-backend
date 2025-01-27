package com.pdds.service;


import com.pdds.config.TestConfig;
import com.pdds.domain.*;
import com.pdds.domain.enums.Role;
import com.pdds.repository.CartRepository;
import com.pdds.repository.ProductRepository;
import com.pdds.repository.UserRepository;
import com.pdds.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CartServiceTest {

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

    private User generateUserForTest(){
        return userRepository.save(new User ("phill.williams@email.com", "password", "Phill Williams", Utils.stringToDate("01/01/1980"), Role.USER));
    }

    @Test
    public void createCartTest(){
        User user = generateUserForTest();
        Cart cart = new Cart(new ArrayList<>(), 0, user);

        Cart createdCart = cartService.create(cart);

        Assertions.assertNotNull(createdCart);
        Assertions.assertEquals(cart.getUserId().getId(), createdCart.getUserId().getId());
    }

    @Test
    public void findCartByIdTest(){
        User user = generateUserForTest();
        Cart cart = cartService.create(new Cart(new ArrayList<>(), 0, user));

        Optional<Cart> opt = cartService.findById(cart.getId());
        Assertions.assertTrue(opt.isPresent());

        Cart foundCart = opt.get();
        Assertions.assertEquals(cart.getUserId().getId(), foundCart.getUserId().getId());

    }

    @Test
    public void findCartByUserTest(){
        User user = (User) userService.findByEmail("john.doe@example.com");
        Optional<Cart> opt = cartService.findByUser(user);
        Assertions.assertTrue(opt.isPresent());

        Cart foundCart = opt.get();
        Assertions.assertEquals(user.getId(), foundCart.getUserId().getId());

    }

    @Test
    public void updateCartTest(){
        User user = (User) userService.findByEmail("john.doe@example.com");
        Optional<Cart> opt = cartService.findByUser(user);
        Assertions.assertTrue(opt.isPresent());
        Cart foundCart = opt.get();

        foundCart.setTotal(999.99);
        boolean update = cartService.update(foundCart);

        Assertions.assertTrue(update);
        Cart updatedCart = cartService.findByUser(user).get();

        Assertions.assertEquals(999.99, updatedCart.getTotal());

    }

    @Test
    public void deleteCartByIdTest(){
        User user = generateUserForTest();
        Cart cart = cartService.create(new Cart(new ArrayList<>(), 0, user));

        boolean delete = cartService.delete(cart.getId());

        Assertions.assertTrue(delete);
        Assertions.assertTrue(cartService.findByUser(user).isEmpty());
    }

    @Test
    public void deleteParamCartTest(){
        User user = generateUserForTest();
        Cart cart = cartService.create(new Cart(new ArrayList<>(), 0, user));

        boolean delete = cartService.delete(cart);

        Assertions.assertTrue(delete);
        Assertions.assertTrue(cartService.findByUser(user).isEmpty());
    }

    @Test
    public void addToCartTest(){

        int qt = 5;
        Product product = productService.findByRegex("Nintendo").get(0);
        User user = (User) userService.findByEmail("john.doe@example.com");
        boolean added = cartService.addToCart(product.getId(), qt, user);

        Assertions.assertTrue(added);

        Optional<Cart> opt = cartRepository.findByUser(user);
        Assertions.assertTrue(opt.isPresent());
        Cart cart = opt.get();

        List<SelectedProduct> products = cart.getShoppingCartProducts();

        for (SelectedProduct sp : products){
            if (sp.getProduct().getId() == product.getId()){
                Assertions.assertEquals(qt, sp.getQuantity());
                Assertions.assertEquals((qt*product.getPrice()), sp.getTotal());
            }
        }

    }


    @Test
    public void updateProductInCartTest(){

        int qt1 = 5;
        int qt2 = 5;
        Product product = productService.findByRegex("Nintendo").get(0);
        User user = (User) userService.findByEmail("john.doe@example.com");

        boolean added = cartService.addToCart(product.getId(), qt1, user);
        boolean secondAdd = cartService.addToCart(product.getId(), qt2, user);
        Assertions.assertTrue(added);
        Assertions.assertTrue(secondAdd);

        Optional<Cart> opt = cartRepository.findByUser(user);
        Assertions.assertTrue(opt.isPresent());
        Cart cart = opt.get();

        List<SelectedProduct> products = cart.getShoppingCartProducts();

        for (SelectedProduct sp : products){
            if (sp.getProduct().getId() == product.getId()){
                Assertions.assertEquals((qt1+qt2), sp.getQuantity());
                Assertions.assertEquals(((qt1+qt2)*product.getPrice()), sp.getTotal());
            }
        }

    }

    @Test
    public void removeAllUnitsFromCartTest(){

        //preparation
        int qt = 5;
        Product product = productService.findByRegex("Nintendo").get(0);
        User user = (User) userService.findByEmail("john.doe@example.com");
        boolean added = cartService.addToCart(product.getId(), qt, user);

        Optional<Cart> opt = cartRepository.findByUser(user);
        Cart cart = opt.get();

        // actual test
        boolean remove = cartService.removeFromCart(product.getId(), qt, cart);
        Assertions.assertTrue(remove);

        Cart cartAfterRemoval = cartRepository.findByUser(user).get();
        Assertions.assertTrue(cartAfterRemoval.getShoppingCartProducts().isEmpty());

    }

    @Test
    public void removeSomeUnitsFromCartTest(){

        //preparation
        int qtToAdd = 5;
        int qtToRemove = 2;
        Product product = productService.findByRegex("Nintendo").get(0);
        User user = (User) userService.findByEmail("john.doe@example.com");
        cartService.addToCart(product.getId(), qtToAdd, user);

        Optional<Cart> opt = cartRepository.findByUser(user);
        Cart cart = opt.get();


        //remove - actual test
        boolean remove = cartService.removeFromCart(product.getId(), qtToRemove, cart);
        Assertions.assertTrue(remove);

        Cart cartAfterRemoval = cartRepository.findByUser(user).get();
        Assertions.assertFalse(cartAfterRemoval.getShoppingCartProducts().isEmpty());

        SelectedProduct selectedProduct = cartAfterRemoval.getShoppingCartProducts().get(0);
        Assertions.assertEquals(product.getName(), selectedProduct.getProduct().getName());
        Assertions.assertEquals((qtToAdd-qtToRemove), selectedProduct.getQuantity());

    }

    @Test
    public void failRemoveFromCartMoreUnitsThanAvailableTest(){

        //preparation
        int qtToAdd = 5;
        int qtToRemove = 9;
        Product product = productService.findByRegex("Nintendo").get(0);
        User user = (User) userService.findByEmail("john.doe@example.com");
        cartService.addToCart(product.getId(), qtToAdd, user);

        Optional<Cart> opt = cartRepository.findByUser(user);
        Cart cart = opt.get();


        //remove - actual test
        boolean remove = cartService.removeFromCart(product.getId(), qtToRemove, cart);
        Assertions.assertFalse(remove);

        Cart cartAfterAttempt = cartRepository.findByUser(user).get();
        Assertions.assertFalse(cartAfterAttempt.getShoppingCartProducts().isEmpty());

        SelectedProduct selectedProduct = cartAfterAttempt.getShoppingCartProducts().get(0);
        Assertions.assertEquals(product.getName(), selectedProduct.getProduct().getName());
        Assertions.assertEquals(qtToAdd, selectedProduct.getQuantity());

    }



}
