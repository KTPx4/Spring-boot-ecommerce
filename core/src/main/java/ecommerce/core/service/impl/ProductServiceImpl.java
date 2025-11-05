package ecommerce.core.service.impl;

import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import ecommerce.core.infra.ProductClientService;
import ecommerce.core.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClientService productClientService;

    public ProductServiceImpl(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }

    @Override
    public ProductCoreResponse createProduct(ProductCoreRequest request) {

        return productClientService.createProduct(request);
    }

    @Override
    public List<ProductCoreResponse> getAllProducts() {
        return productClientService.getAllProduct();
    }
}
