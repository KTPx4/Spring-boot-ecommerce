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

    @Override
    public ProductCoreResponse deleteProduct(Long id) {
        log.info("infra - start deleteProduct");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productRepository.delete(product);

        log.info("infra - end deleteProduct");
        return ProductInfraMapper.toProductCoreResponse(product);
    }

    @Override
    public ProductCoreResponse updateProduct(Long id, ProductCoreRequest request) {
        log.info("infra - start updateProduct");
        
        // Check product exists
        productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Brand brand = brandRepository.getReferenceById(request.getBrandId());
        Category category = categoryRepository.getReferenceById(request.getCategoryId());

        var updateProduct = ProductInfraMapper.toUpdateEntity(id, request, brand, category);

        return ProductInfraMapper.toProductCoreResponse(productRepository.save(updateProduct));
    }

    @Override
    public boolean existsById(Long productId) {
        log.info("infra - checking if product exists: {}", productId);
        return productRepository.existsById(productId);
    }

    @Override
    public boolean existsBySlug(String slug) {
        log.info("infra - checking if slug exists: {}", slug);
        return productRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsBySku(String sku) {
        log.info("infra - checking if SKU exists: {}", sku);
        return productRepository.existsBySku(sku);
    }

}
