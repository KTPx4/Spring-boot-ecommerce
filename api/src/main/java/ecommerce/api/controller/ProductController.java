package ecommerce.api.controller;

import ecommerce.api.dto.ProductApiRequest;
import ecommerce.api.mapper.ProductMapper;

import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import ecommerce.core.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@Tag(name = "Product", description = "Product management APIs - Public GET, Admin required for CUD operations")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Create new product", description = "Create a new product. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    public ResponseEntity<?> createProduct(
            @Parameter(description = "Product details", required = true) @RequestBody ProductApiRequest product) {
        log.info("REST request to save Product : {}", product);
        ProductCoreRequest proC = ProductMapper.toProductCoreRequest(product);
        log.info("Start call productService ");
        ProductCoreResponse res = productService.createProduct(proC);
        return ResponseBuilder.success("Create product success", res);
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get all products", description = "Retrieve all products. Public endpoint - No authentication required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<?> getAllProducts() {
        log.info("REST request to get all Products");
        List<ProductCoreResponse> res = productService.getAllProducts();
        return ResponseBuilder.success("Get all products success", res);
    }

    @GetMapping("/{id}")
    @PermitAll
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID. Public endpoint - No authentication required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> getProductById(
            @Parameter(description = "Product ID", required = true, example = "1") @PathVariable Long id) {
        log.info("REST request to get Product by ID : {}", id);
        ProductCoreResponse res = productService.getProductById(id);
        return ResponseBuilder.success("Get product success", res);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update product", description = "Update an existing product. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> updateProduct(
            @Parameter(description = "Product ID", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Updated product details", required = true) @RequestBody ProductApiRequest product) {
        log.info("REST request to update Product : {}", id);
        ProductCoreRequest proC = ProductMapper.toProductCoreRequest(product);
        ProductCoreResponse res = productService.updateProduct(id, proC);
        return ResponseBuilder.success("Update product success", res);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete product", description = "Delete a product by ID. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "Product ID", required = true, example = "1") @PathVariable Long id) {
        log.info("REST request to delete Product : {}", id);
        ProductCoreResponse res = productService.deleteProduct(id);
        return ResponseBuilder.success("Delete product success", res);
    }
}
