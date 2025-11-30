package ecommerce.core.service.impl;

import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import ecommerce.core.infra.ProductClientService;
import ecommerce.core.service.ProductService;
import ecommerce.core.service.validation.BrandValidationService;
import ecommerce.core.service.validation.CategoryValidationService;
import ecommerce.core.service.validation.ProductValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClientService productClientService;
    private final BrandValidationService brandValidationService;
    private final CategoryValidationService categoryValidationService;
    private final ProductValidationService productValidationService;

    @Override
    public ProductCoreResponse createProduct(ProductCoreRequest request) {
        log.info("Create product at ProductServiceImpl run");
        validateCreateRequest(request);
        log.info(" ProductServiceImpl run return ");
        return productClientService.createProduct(request);
    }

    @Override
    public List<ProductCoreResponse> getAllProducts() {
        return productClientService.getAllProduct();
    }

    @Override
    public ProductCoreResponse getProductById(Long id) {
        log.info("Get product by ID at ProductServiceImpl: {}", id);
        productValidationService.validateProductExists(id);
        return productClientService.getProductById(id);
    }

    @Override
    public ProductCoreResponse updateProduct(Long id, ProductCoreRequest request) {
        log.info("Update product at ProductServiceImpl: {}", id);
        validateUpdateRequest(id, request);
        return productClientService.updateProduct(id, request);
    }

    @Override
    public ProductCoreResponse deleteProduct(Long id) {
        log.info("Delete product at ProductServiceImpl: {}", id);
        productValidationService.validateProductExists(id);
        return productClientService.deleteProduct(id);
    }

    private void validateCreateRequest(ProductCoreRequest request) {
        log.info("Start validateCreateRequest");

        // Validate input fields
        if (request.getBrandId() == null || request.getBrandId() == 0)
            throw new IllegalArgumentException("Invalid brandId");
        if (request.getCategoryId() == null || request.getCategoryId() == 0)
            throw new IllegalArgumentException("Invalid categoryId");
        if (request.getName() == null || request.getName().equals(""))
            throw new IllegalArgumentException("Invalid name");
        if (request.getPrice() == null || request.getPrice() <= 0)
            throw new IllegalArgumentException("Invalid price");

        // Validate brand and category exist
        brandValidationService.validateBrandExists(request.getBrandId());
        categoryValidationService.validateCategoryExists(request.getCategoryId());

        // Validate slug and SKU uniqueness
        productValidationService.validateSlugNotExists(request.getSlug());
        productValidationService.validateSkuNotExists(request.getSku());

        log.info("End validateCreateRequest");
    }

    private void validateUpdateRequest(Long id, ProductCoreRequest request) {
        log.info("Start validateUpdateRequest");

        // Validate product exists
        productValidationService.validateProductExists(id);

        // Validate input fields
        if (request.getBrandId() == null || request.getBrandId() == 0)
            throw new IllegalArgumentException("Invalid brandId");
        if (request.getCategoryId() == null || request.getCategoryId() == 0)
            throw new IllegalArgumentException("Invalid categoryId");
        if (request.getName() == null || request.getName().equals(""))
            throw new IllegalArgumentException("Invalid name");
        if (request.getPrice() == null || request.getPrice() <= 0)
            throw new IllegalArgumentException("Invalid price");

        // Validate brand and category exist
        brandValidationService.validateBrandExists(request.getBrandId());
        categoryValidationService.validateCategoryExists(request.getCategoryId());

        log.info("End validateUpdateRequest");
    }
}
