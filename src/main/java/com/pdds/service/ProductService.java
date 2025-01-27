package com.pdds.service;


import com.pdds.domain.Product;
import com.pdds.domain.User;
import com.pdds.dto.ProductDTO;
import com.pdds.repository.ProductRepository;
import com.pdds.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public boolean create(ProductDTO productDTO) {
        Product product = new Product(productDTO.name(), productDTO.price(), productDTO.stock());
        productRepository.save(product);
        return true;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }

    public List<Product> findByRegex(String name){
        return productRepository.findByRegex(name);
    }

    public boolean update(Long id, ProductDTO productDTO){
        Optional<Product> opt = productRepository.findById(id);

        if (opt.isEmpty()) return false;
        Product productToUpdate = opt.get();

        productToUpdate.setName(productDTO.name());
        productToUpdate.setPrice(productDTO.price());
        productToUpdate.setStock(productDTO.stock());

        productRepository.save(productToUpdate);

        return true;
    }

    public boolean delete(Long id){
        Optional<Product> opt = productRepository.findById(id);

        if(opt.isEmpty()) return false;
        productRepository.delete(opt.get());
        return true;
    }

}
