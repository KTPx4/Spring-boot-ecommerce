package ecommerce.core.service.impl;

import ecommerce.core.domain.category.CategoryCoreRequest;
import ecommerce.core.domain.category.CategoryCoreResponse;
import ecommerce.core.infra.CategoryClientService;
import ecommerce.core.service.CategoryService;
import ecommerce.core.service.validation.CategoryValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryClientService categoryClientService;
    private final CategoryValidationService categoryValidationService;

    @Override
    public CategoryCoreResponse createCategory(CategoryCoreRequest request) {
        log.info("Create category at CategoryServiceImpl");
        validateCreateRequest(request);
        return categoryClientService.createCategory(request);
    }

    @Override
    public List<CategoryCoreResponse> getAllCategories() {
        log.info("Get all categories at CategoryServiceImpl");
        return categoryClientService.getAllCategories();
    }

    @Override
    public CategoryCoreResponse getCategoryById(Long id) {
        log.info("Get category by ID at CategoryServiceImpl: {}", id);
        categoryValidationService.validateCategoryExists(id);
        return categoryClientService.getCategoryById(id);
    }

    @Override
    public CategoryCoreResponse updateCategory(Long id, CategoryCoreRequest request) {
        log.info("Update category at CategoryServiceImpl: {}", id);
        validateUpdateRequest(id, request);
        return categoryClientService.updateCategory(id, request);
    }

    @Override
    public CategoryCoreResponse deleteCategory(Long id) {
        log.info("Delete category at CategoryServiceImpl: {}", id);
        categoryValidationService.validateCategoryExists(id);
        return categoryClientService.deleteCategory(id);
    }

    private void validateCreateRequest(CategoryCoreRequest request) {
        log.info("Start validateCreateRequest for Category");

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (request.getSlug() == null || request.getSlug().trim().isEmpty()) {
            throw new IllegalArgumentException("Category slug is required");
        }

        log.info("End validateCreateRequest for Category");
    }

    private void validateUpdateRequest(Long id, CategoryCoreRequest request) {
        log.info("Start validateUpdateRequest for Category");

        categoryValidationService.validateCategoryExists(id);

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (request.getSlug() == null || request.getSlug().trim().isEmpty()) {
            throw new IllegalArgumentException("Category slug is required");
        }

        log.info("End validateUpdateRequest for Category");
    }
}
