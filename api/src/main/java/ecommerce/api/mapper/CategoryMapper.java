package ecommerce.api.mapper;

import ecommerce.api.dto.CategoryApiRequest;
import ecommerce.api.dto.CategoryApiResponse;
import ecommerce.core.domain.category.CategoryCoreRequest;
import ecommerce.core.domain.category.CategoryCoreResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryCoreRequest toCategoryCoreRequest(CategoryApiRequest apiRequest) {
        CategoryCoreRequest coreRequest = new CategoryCoreRequest();
        coreRequest.setName(apiRequest.getName());
        coreRequest.setSlug(apiRequest.getSlug());
        coreRequest.setParentId(apiRequest.getParent_id());
        return coreRequest;
    }

    public static CategoryApiResponse toCategoryApiResponse(CategoryCoreResponse coreResponse) {
        CategoryApiResponse apiResponse = new CategoryApiResponse();
        apiResponse.setId(coreResponse.getId());
        apiResponse.setName(coreResponse.getName());
        apiResponse.setSlug(coreResponse.getSlug());
        apiResponse.setParent_id(coreResponse.getParentId());

        // Map children if exists
        if (coreResponse.getChildren() != null && !coreResponse.getChildren().isEmpty()) {
            List<CategoryApiResponse> children = coreResponse.getChildren().stream()
                    .map(CategoryMapper::toCategoryApiResponse)
                    .collect(Collectors.toList());
            apiResponse.setChildren(children);
        } else {
            apiResponse.setChildren(Collections.emptyList());
        }

        return apiResponse;
    }
}
