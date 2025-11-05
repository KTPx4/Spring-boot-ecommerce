package ecommerce.infra.client.product.service;

import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import ecommerce.core.infra.ProductClientService;
import ecommerce.infra.client.product.entity.Brand;
import ecommerce.infra.client.product.entity.Category;
import ecommerce.infra.client.product.entity.Product;
import ecommerce.infra.client.product.mapper.ProductInfraMapper;
import ecommerce.infra.client.product.repository.BrandRepository;
import ecommerce.infra.client.product.repository.CategoryRepository;
import ecommerce.infra.client.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductClientServiceImpl implements ProductClientService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    @Override
    public ProductCoreResponse createProduct(ProductCoreRequest request) {
            log.info("infra - start createProduct");

            if(!brandRepository.existsById(request.getBrandId()))
                throw new IllegalArgumentException("Brand does not exist");
            if(!categoryRepository.existsById(request.getCategoryId()))
                throw new IllegalArgumentException("Category does not exist");

            Brand brand = brandRepository.getReferenceById(request.getBrandId());
            Category category = categoryRepository.getReferenceById(request.getCategoryId());
            Product product = ProductInfraMapper.toEntity(request, brand, category);
            Product productSave  = productRepository.save(product);

            log.info("infra - end createProduct");
        return ProductInfraMapper.toProductCoreResponse(productSave);
    }

    @Override
    public List<ProductCoreResponse> getAllProduct() {
        log.info("infra - start getAllProduct");
        List<Product> products = productRepository.findAll();
        List<ProductCoreResponse> responses = products.stream().map(ProductInfraMapper ::toProductCoreResponse).toList();
        return responses;
    }
}
