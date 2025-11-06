package ecommerce.core.service;

import ecommerce.core.domain.category.CategoryCoreRequest;
import ecommerce.core.domain.category.CategoryCoreResponse;

import java.util.List;

public interface CategoryService {
    CategoryCoreResponse createCategory(CategoryCoreRequest request);

    List<CategoryCoreResponse> getAllCategories();

    CategoryCoreResponse getCategoryById(Long id);

    CategoryCoreResponse updateCategory(Long id, CategoryCoreRequest request);

    CategoryCoreResponse deleteCategory(Long id);
}
