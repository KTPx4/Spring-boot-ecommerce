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
        log.info("REST request to save Product : {}", product);
        ProductCoreRequest proC = ProductMapper.toProductCoreRequest(product);
        log.info("Start call productService ");
        ProductCoreResponse res = productService.createProduct(proC);
        return ResponseBuilder.success("Create product success", res);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        log.info("REST request to get all Products");
        List<ProductCoreResponse> res = productService.getAllProducts();
        return ResponseBuilder.success("Get all products success", res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        log.info("REST request to get Product by ID : {}", id);
        ProductCoreResponse res = productService.getProductById(id);
        return ResponseBuilder.success("Get product success", res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductApiRequest product) {
        log.info("REST request to update Product : {}", id);
        ProductCoreRequest proC = ProductMapper.toProductCoreRequest(product);
        ProductCoreResponse res = productService.updateProduct(id, proC);
        return ResponseBuilder.success("Update product success", res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete Product : {}", id);
        ProductCoreResponse res = productService.deleteProduct(id);
        return ResponseBuilder.success("Delete product success", res);
    }
}
