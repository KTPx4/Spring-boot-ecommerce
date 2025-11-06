package ecommerce.api.controller;

import ecommerce.api.dto.ProductApiRequest;
import ecommerce.api.dto.ProductApiResponse;
import ecommerce.api.mapper.ProductMapper;


import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import ecommerce.core.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductApiRequest product) {
        log.info("REST request to save Product : {}");
        ProductCoreRequest proC = ProductMapper.toProductCoreRequest(product);
        log.info("Start call productSerivice ");
        ProductCoreResponse res = productService.createProduct(proC);
        return ResponseBuilder.success("Create product success", res);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {

        log.info("REST request to get all Products");
        List<ProductCoreResponse> res = productService.getAllProducts();
        log.info("REST request to get all Products" + res.stream().toString());
        return  ResponseBuilder.success("Create product success", res);
    }
}
