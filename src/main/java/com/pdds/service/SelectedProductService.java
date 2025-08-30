package com.pdds.service;

import com.pdds.domain.*;
import com.pdds.domain.enums.SelectedProductOperation;
import com.pdds.repository.SelectedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SelectedProductService {

    private final SelectedProductRepository selectedProductRepository;

    @Autowired
    SelectedProductService(SelectedProductRepository selectedProductRepository){
        this.selectedProductRepository = selectedProductRepository;
    }

    public SelectedProduct create(SelectedProduct selectedProduct){
        return selectedProductRepository.save(selectedProduct);
    }

    public List<SelectedProduct> getAll(){
        return selectedProductRepository.findAll();
    }

    public Optional<SelectedProduct> findById(Long id){
        return selectedProductRepository.findById(id);
    }

    public List<SelectedProduct> findByCart(Cart cart){
        return selectedProductRepository.findByCart(cart);
    }

    public List<SelectedProduct> findByOrder(Order order){
        return selectedProductRepository.findByOrder(order);
    }

    public void update(SelectedProduct selectedProduct){
        selectedProductRepository.save(selectedProduct);
    }

    public void updateQuantity(SelectedProduct selectedProduct, int newQuantity){
        selectedProduct.setQuantity(newQuantity);
        selectedProduct.setTotal(newQuantity * selectedProduct.getProduct().getPrice());
        selectedProductRepository.save(selectedProduct);
    }

    public void delete(SelectedProduct selectedProduct){
        selectedProductRepository.delete(selectedProduct);
    }

}
