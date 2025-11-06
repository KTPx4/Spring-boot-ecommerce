package ecommerce.api.mapper;

import ecommerce.api.dto.BrandApiRequest;
import ecommerce.api.dto.BrandApiResponse;
import ecommerce.core.domain.brand.BrandCoreRequest;
import ecommerce.core.domain.brand.BrandCoreResponse;

public class BrandMapper {

    public static BrandCoreRequest toBrandCoreRequest(BrandApiRequest apiRequest) {
        BrandCoreRequest coreRequest = new BrandCoreRequest();
        coreRequest.setName(apiRequest.getName());
        coreRequest.setSlug(apiRequest.getSlug());
        coreRequest.setLogoUrl(apiRequest.getLogo_url());
        return coreRequest;
    }

    public static BrandApiResponse toBrandApiResponse(BrandCoreResponse coreResponse) {
        BrandApiResponse apiResponse = new BrandApiResponse();
        apiResponse.setId(coreResponse.getId());
        apiResponse.setName(coreResponse.getName());
        apiResponse.setSlug(coreResponse.getSlug());
        apiResponse.setLogo_url(coreResponse.getLogoUrl());
        apiResponse.setCreated_at(coreResponse.getCreatedAt());
        apiResponse.setUpdated_at(coreResponse.getUpdatedAt());
        return apiResponse;
    }
}
