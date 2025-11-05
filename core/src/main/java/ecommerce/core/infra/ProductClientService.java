package ecommerce.core.infra;

import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductClientService {
    ProductCoreResponse createProduct(ProductCoreRequest request);
    List<ProductCoreRequest> getAllProduct();
}
