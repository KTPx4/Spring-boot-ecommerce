package ecommerce.core.service.validation;

import ecommerce.core.infra.CategoryClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryValidationService {
    private final CategoryClientService categoryClientService;

    /**
     * Check if category exists by ID
     * @param categoryId the category ID
     * @throws IllegalArgumentException if category not found
     */
    public void validateCategoryExists(Long categoryId) {
        log.info("Validating category exists: {}", categoryId);
        if (!categoryClientService.existsById(categoryId)) {
            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
        }
    }
}
