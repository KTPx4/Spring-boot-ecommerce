package ecommerce.infra.client.category.mapper;

import ecommerce.core.domain.category.CategoryCoreRequest;
import ecommerce.core.domain.category.CategoryCoreResponse;
import ecommerce.infra.client.category.entity.Category;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryInfraMapper {

    public static Category toEntity(CategoryCoreRequest request, Category parent) {
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setParent(parent);
        return category;
    }

    public static Category toUpdateEntity(Long id, CategoryCoreRequest request, Category parent) {
        Category category = new Category();
        category.setId(id);
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setParent(parent);
        return category;
    }

    public static CategoryCoreResponse toCategoryCoreResponse(Category category) {
        CategoryCoreResponse response = new CategoryCoreResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setParentId(category.getParent() != null ? category.getParent().getId() : null);

        // Map children if exists
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryCoreResponse> children = category.getChildren().stream()
                    .map(CategoryInfraMapper::toCategoryCoreResponse)
                    .collect(Collectors.toList());
            response.setChildren(children);
        } else {
            response.setChildren(Collections.emptyList());
        }

        return response;
    }
}
