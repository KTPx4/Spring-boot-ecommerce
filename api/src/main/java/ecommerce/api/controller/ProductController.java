package ecommerce.api.controller;

import ecommerce.api.dto.ProductRequest;
import ecommerce.api.dto.ProductResponse;
import ecommerce.api.mapper.ProductMapper;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private ProductMapper mapper;
    public ProductController(  ProductMapper mapper) {

        this.mapper = mapper;
    }
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {

        return null;
    }


}
