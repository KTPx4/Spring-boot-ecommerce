package ecommerce.core.service.validation;

import ecommerce.core.infra.ProductClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductValidationService {
    private final ProductClientService productClientService;

    /**
     * Check if product exists by ID
     * @param productId the product ID
     * @throws IllegalArgumentException if product not found
     */
    public void validateProductExists(Long productId) {
        log.info("Validating product exists: {}", productId);
        if (!productClientService.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
    }

    /**
     * Check if slug already exists
     * @param slug the product slug
     * @throws IllegalArgumentException if slug already exists
     */
    public void validateSlugNotExists(String slug) {
        log.info("Validating slug not exists: {}", slug);
        if (productClientService.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug already exists: " + slug);
        }
    }

    /**
     * Check if SKU already exists
     * @param sku the product SKU
     * @throws IllegalArgumentException if SKU already exists
     */
    public void validateSkuNotExists(String sku) {
        log.info("Validating SKU not exists: {}", sku);
        if (productClientService.existsBySku(sku)) {
            throw new IllegalArgumentException("SKU already exists: " + sku);
        }
    }
}
