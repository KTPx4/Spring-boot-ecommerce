package ecommerce.infra.client.mapper;

import ecommerce.core.domain.brand.BrandCoreRequest;
import ecommerce.core.domain.brand.BrandCoreResponse;
import ecommerce.infra.client.entity.Brand;

public class BrandInfraMapper {

    public static Brand toEntity(BrandCoreRequest request) {
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setSlug(request.getSlug());
        brand.setLogo_url(request.getLogoUrl());
        return brand;
    }

    public static Brand toUpdateEntity(Long id, BrandCoreRequest request) {
        Brand brand = new Brand();
        brand.setId(id);
        brand.setName(request.getName());
        brand.setSlug(request.getSlug());
        brand.setLogo_url(request.getLogoUrl());
        return brand;
    }

    public static BrandCoreResponse toBrandCoreResponse(Brand brand) {
        BrandCoreResponse response = new BrandCoreResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setSlug(brand.getSlug());
        response.setLogoUrl(brand.getLogo_url());
        response.setCreatedAt(brand.getCreatedAt());
        response.setUpdatedAt(brand.getUpdatedAt());
        return response;
    }
}
