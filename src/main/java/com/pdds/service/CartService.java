package com.pdds.service;

import com.pdds.domain.*;
import com.pdds.domain.enums.OrderStatus;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final SelectedProductService selectedProductService;

    @Autowired
    CartService(CartRepository cartRepository, ProductService productService, SelectedProductService selectedProductService){
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.selectedProductService = selectedProductService;
    }

    public Cart create(Cart cart){
        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(Long id){
        return cartRepository.findById(id);
    }

    public Optional<Cart> findByUser(User user){
        return cartRepository.findByUser(user);
    }

    public boolean update(Cart cart){
        cartRepository.save(cart);
        return true;
    }

    public boolean delete(Cart cart){
        cartRepository.delete(cart);
        return true;
    }

    public boolean delete(Long id){
        Optional<Cart> opt = cartRepository.findById(id);

        if(opt.isEmpty()) return false;
        cartRepository.delete(opt.get());
        return true;
    }

    public boolean addToCart(Long productId, int qt, User principal){
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) return false;

        Product product = optProduct.get();
        double total = product.getPrice() * qt;

        Optional<Cart> optCart = findByUser(principal);
        if (optCart.isEmpty()) return false;

        Cart cart = optCart.get();
        List<SelectedProduct> selProds = cart.getShoppingCartProducts();

        for(SelectedProduct selProd : selProds){
            if (selProd.getProduct().getId() == product.getId()) {

                int newQty = selProd.getQuantity() + qt;
                double newTotal = newQty * selProd.getProduct().getPrice();

                selProd.setQuantity(newQty);
                selProd.setTotal(newTotal);

                selectedProductService.update(selProd);
                return true;
            }
        }

        SelectedProduct sp = new SelectedProduct(
                SelectedProductOperation.CART,
                qt,
                product,
                cart
        );

        selProds.add(selectedProductService.create(sp));
        cart.setShoppingCartProducts(selProds);
        cartRepository.save(cart);
        return true;
    }

    public boolean removeFromCart(Long productId, int qt, Cart cart){

        if (qt<0) return false;

        List<SelectedProduct> products = cart.getShoppingCartProducts();
        for (SelectedProduct selectedProduct : products){
            if (selectedProduct.getProduct().getId() == productId){

                if(qt > selectedProduct.getQuantity()) return false;

                if (qt == selectedProduct.getQuantity()){

                    selectedProductService.delete(selectedProduct);
                    products.remove(selectedProduct);
                    cart.setShoppingCartProducts(products);
                    update(cart);

                    return true;
                }

                selectedProduct.setQuantity(selectedProduct.getQuantity() - qt);
                selectedProduct.setTotal(qt * selectedProduct.getProduct().getPrice());

                cart.setShoppingCartProducts(products);
                update(cart);

                selectedProductService.update(selectedProduct);
                return true;
            }
        }

        return false;

    }



}
