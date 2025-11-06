package ecommerce.core.infra;

import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductClientService {
    ProductCoreResponse createProduct(ProductCoreRequest request);

    List<ProductCoreResponse> getAllProduct();

    ProductCoreResponse getProductById(Long id);

    ProductCoreResponse deleteProduct(Long id);

    ProductCoreResponse updateProduct(Long id, ProductCoreRequest request);

    // Validation methods
    boolean existsById(Long productId);

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);
}
