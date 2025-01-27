package com.pdds.service;

import com.pdds.domain.Cart;
import com.pdds.domain.Order;
import com.pdds.domain.SelectedProduct;
import com.pdds.domain.User;
import com.pdds.domain.enums.OrderStatus;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final SelectedProductService selectedProductService;

    @Autowired
    OrderService (OrderRepository orderRepository, SelectedProductService selectedProductService){
        this.orderRepository = orderRepository;
        this.selectedProductService = selectedProductService;
    }

    public boolean create(Order order){
        Order savedOrder = orderRepository.save(order);
        List<SelectedProduct> selectedProducts = savedOrder.getOrderProducts();

        for (SelectedProduct sp : selectedProducts){
            sp.setOrder(savedOrder);
            sp.setOperation(SelectedProductOperation.ORDER);
            selectedProductService.update(sp);
        }

        return true;

    }

    public List<Order> findByUser(User user){
        return orderRepository.findByUser(user);
    }

    @Transactional
    public boolean generateOrder(Cart cart) {
        List<SelectedProduct> orderProducts = cart.getShoppingCartProducts().stream()
                .map(sp -> new SelectedProduct(
                        sp.getOperation(),
                        sp.getQuantity(),
                        sp.getProduct(),
                        null  // cart será null pois agora está no order
                ))
                .toList();

        Order order = new Order(
                orderProducts,
                OrderStatus.PENDING,
                cart.getUserId()
        );

        // Importante: setar a referência do order em cada SelectedProduct
        orderProducts.forEach(sp -> sp.setOrder(order));

        create(order);

        // Opcional: limpar o carrinho após criar o order
        cart.getShoppingCartProducts().clear();
        cart.setTotal(0);
        // Salvar o carrinho atualizado

        return true;
    }

}
