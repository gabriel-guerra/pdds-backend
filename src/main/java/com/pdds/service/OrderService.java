package com.pdds.service;

import com.pdds.domain.*;
import com.pdds.domain.enums.OrderStatus;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.dto.SelectedProductResponseDTO;
import com.pdds.dto.UpdateOrderDTO;
import com.pdds.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final SelectedProductService selectedProductService;
    private final ProductService productService;

    @Autowired
    OrderService (OrderRepository orderRepository, SelectedProductService selectedProductService, ProductService productService){
        this.orderRepository = orderRepository;
        this.selectedProductService = selectedProductService;
        this.productService = productService;
    }

    public Order create(Order order){
        Order savedOrder = orderRepository.save(order);
        List<SelectedProduct> selectedProducts = savedOrder.getOrderProducts();

        for (SelectedProduct sp : selectedProducts){
            sp.setOrder(savedOrder);
            sp.setOperation(SelectedProductOperation.ORDER);
            selectedProductService.update(sp);
        }

        return savedOrder;

    }

    public List<Order> findByUser(User user){
        return orderRepository.findByUser(user);
    }

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    }

    public boolean changeStatus(Order order, OrderStatus status){
        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }

    public Order update(Order order, List<UpdateOrderDTO> newProducts){

        for (UpdateOrderDTO dto : newProducts){
            boolean productFound = false;

            for(SelectedProduct sp: order.getOrderProducts()){
                if (sp.getProduct().getId() == dto.productId()){
                    selectedProductService.updateQuantity(sp, dto.quantity());
                    productFound = true;
                }
            }

            if (!productFound){
                Optional<Product> opt = productService.findById(dto.productId());
                if (opt.isEmpty()) return null;

                SelectedProduct newProduct = selectedProductService.create(new SelectedProduct(
                        SelectedProductOperation.ORDER,
                        dto.quantity(),
                        opt.get(),
                        null
                ));

                order.getOrderProducts().add(newProduct);

            }
        }

        order.setTotal(recalculateTotal(order));
        return orderRepository.save(order);

    }

    public boolean deleteById(Long id){
        Optional<Order> opt = findById(id);
        if (opt.isEmpty()) return false;

        orderRepository.delete(opt.get());
        return true;
    }


    @Transactional
    public Order generateOrder(Cart cart) {
        List<SelectedProduct> orderProducts = cart.getShoppingCartProducts().stream()
                .map(sp -> new SelectedProduct(
                        sp.getOperation(),
                        sp.getQuantity(),
                        sp.getProduct(),
                        null
                ))
                .toList();

        Order order = new Order(
                orderProducts,
                OrderStatus.PENDING,
                cart.getUserId()
        );

        Order createdOrder = create(order);

        cart.getShoppingCartProducts().clear();
        cart.setTotal(0);

        return createdOrder;
    }

    public List<SelectedProductResponseDTO> listOfItems(Order order){

        List<SelectedProductResponseDTO> listOfItems = new ArrayList<>();

        for (SelectedProduct sp : order.getOrderProducts()){
            SelectedProductResponseDTO dto = new SelectedProductResponseDTO(sp.getId(), sp.getOperation(), sp.getQuantity(), sp.getTotal(), sp.getProduct());
            listOfItems.add(dto);
        }

        return listOfItems;

    }

    public double recalculateTotal(Order order){

        double total = 0.0;

        for (SelectedProduct sp : order.getOrderProducts()){
            total += sp.getTotal();
        }

        return total;

    }

}
