package ecommerce.core.service.validation;

import ecommerce.core.infra.BrandClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandValidationService {
    private final BrandClientService brandClientService;

    /**
     * Check if brand exists by ID
     * @param brandId the brand ID
     * @throws IllegalArgumentException if brand not found
     */
    public void validateBrandExists(Long brandId) {
        log.info("Validating brand exists: {}", brandId);
        if (!brandClientService.existsById(brandId)) {
            throw new IllegalArgumentException("Brand not found with ID: " + brandId);
        }
    }
}
