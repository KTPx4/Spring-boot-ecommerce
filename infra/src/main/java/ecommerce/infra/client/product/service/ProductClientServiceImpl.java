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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

            if(!brandRepository.existsById(request.getBrand_id()))
                throw new IllegalArgumentException("Brand does not exist");
            if(!categoryRepository.existsById(request.getCategory_id()))
                throw new IllegalArgumentException("Category does not exist");

            Brand brand = brandRepository.getReferenceById(request.getBrand_id());
            Category category = categoryRepository.getReferenceById(request.getCategory_id());
            Product product = ProductInfraMapper.toEntity(request, brand, category);
            Product productSave  = productRepository.save(product);

            log.info("infra - end createProduct");
        return ProductInfraMapper.toProductCoreResponse(productSave);
    }

    @Override
    public List<ProductCoreRequest> getAllProduct() {
        log.info("infra - start getAllProduct");
        List<Product> products = productRepository.findAll();
        List<ProductCoreResponse> responses = products.stream().map(ProductInfraMapper ::toProductCoreResponse).toList();


        return List.of();
    }
}
