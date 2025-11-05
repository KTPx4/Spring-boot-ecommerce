package ecommerce.core.service.impl;

import ecommerce.core.infra.ProductClientService;
import ecommerce.core.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClientService productClientService;

    public ProductServiceImpl(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }
}
