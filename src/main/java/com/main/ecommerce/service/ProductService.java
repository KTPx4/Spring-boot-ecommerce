package com.main.ecommerce.service;

import com.main.ecommerce.model.Product;
import com.main.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(int id) {
        return productRepository.findById(id).map(product -> product).orElse(null);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
    public void delete(Product product) {
        productRepository.delete(product);
    }

}
