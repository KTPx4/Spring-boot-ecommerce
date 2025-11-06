package ecommerce.core.service;

import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;

import java.util.List;

public interface ProductService {
    ProductCoreResponse createProduct(ProductCoreRequest request);

    List<ProductCoreResponse> getAllProducts();

    ProductCoreResponse getProductById(Long id);

    ProductCoreResponse updateProduct(Long id, ProductCoreRequest request);

    ProductCoreResponse deleteProduct(Long id);
}
