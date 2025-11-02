package com.main.ecommerce.controller;

import com.main.ecommerce.dto.ProductDTO;
import com.main.ecommerce.model.Product;
import com.main.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public List<ProductDTO> getProducts() {
        return convertProductDTOs(productService.findAll());

    }
    @PostMapping("/")
    public Product postProduct(@RequestBody ProductDTO productDTO) {
        Product product = new Product(productDTO);
        return productService.save(product);
    }

    private List<ProductDTO> convertProductDTOs(List<Product> products) {
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO(product);

        }
        return productDTOs;
    }
}
