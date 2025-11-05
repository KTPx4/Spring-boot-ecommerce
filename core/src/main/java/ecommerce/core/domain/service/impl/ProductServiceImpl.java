package ecommerce.core.domain.service.impl;

import ecommerce.core.domain.infra.ProductClientService;
import ecommerce.core.domain.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductClientService productClientService;


}
