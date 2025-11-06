package ecommerce.infra.client.category.service;

import ecommerce.core.domain.category.CategoryCoreRequest;
import ecommerce.core.domain.category.CategoryCoreResponse;
import ecommerce.core.infra.CategoryClientService;
import ecommerce.infra.client.category.mapper.CategoryInfraMapper;
import ecommerce.infra.client.category.entity.Category;
import ecommerce.infra.client.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryClientServiceImpl implements CategoryClientService {
    private final CategoryRepository categoryRepository;

    @Override
    public boolean existsById(Long categoryId) {
        log.info("Checking if category exists: {}", categoryId);
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public CategoryCoreResponse createCategory(CategoryCoreRequest request) {
        log.info("infra - start createCategory");

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
        }

        Category category = CategoryInfraMapper.toEntity(request, parent);
        Category savedCategory = categoryRepository.save(category);

        log.info("infra - end createCategory");
        return CategoryInfraMapper.toCategoryCoreResponse(savedCategory);
    }

    @Override
    public List<CategoryCoreResponse> getAllCategories() {
        log.info("infra - start getAllCategories");
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryInfraMapper::toCategoryCoreResponse)
                .filter(categoryCoreResponse -> categoryCoreResponse.getParentId() == null)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryCoreResponse getCategoryById(Long id) {
        log.info("infra - start getCategoryById: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return CategoryInfraMapper.toCategoryCoreResponse(category);
    }

    @Override
    public CategoryCoreResponse updateCategory(Long id, CategoryCoreRequest request) {
        log.info("infra - start updateCategory: {}", id);

        // Check if exists
        categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
        }

        Category category = CategoryInfraMapper.toUpdateEntity(id, request, parent);
        Category updatedCategory = categoryRepository.save(category);

        log.info("infra - end updateCategory");
        return CategoryInfraMapper.toCategoryCoreResponse(updatedCategory);
    }

    @Override
    public CategoryCoreResponse deleteCategory(Long id) {
        log.info("infra - start deleteCategory: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        categoryRepository.delete(category);

        log.info("infra - end deleteCategory");
        return CategoryInfraMapper.toCategoryCoreResponse(category);
    }

    @Override
    public boolean existsBySlug(String slug) {
        log.info("infra - checking if category slug exists: {}", slug);
        return categoryRepository.existsBySlug(slug);
    }
}
