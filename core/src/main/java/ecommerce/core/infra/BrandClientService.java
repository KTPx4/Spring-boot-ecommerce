package ecommerce.core.infra;

import ecommerce.core.domain.brand.BrandCoreRequest;
import ecommerce.core.domain.brand.BrandCoreResponse;

import java.util.List;

public interface BrandClientService {
    boolean existsById(Long brandId);

    BrandCoreResponse createBrand(BrandCoreRequest request);

    List<BrandCoreResponse> getAllBrands();

    BrandCoreResponse getBrandById(Long id);

    BrandCoreResponse updateBrand(Long id, BrandCoreRequest request);

    BrandCoreResponse deleteBrand(Long id);

    boolean existsBySlug(String slug);
}
