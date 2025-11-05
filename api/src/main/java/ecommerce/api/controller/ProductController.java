package ecommerce.api.controller;

import ecommerce.api.dto.ProductApiRequest;
import ecommerce.api.dto.ProductApiResponse;
import ecommerce.api.mapper.ProductMapper;


import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import ecommerce.core.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private  ProductService productService;
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductApiRequest product) {
        ProductCoreRequest proC = ProductMapper.toProductCoreRequest(product);
        ProductCoreResponse res = productService.createProduct(proC);
        return ResponseBuilder.success("Create product success", res);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductCoreResponse> res = productService.getAllProducts();
        return  ResponseBuilder.success("Create product success", res);
    }
}
