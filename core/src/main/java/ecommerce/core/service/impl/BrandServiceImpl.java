package ecommerce.core.service.impl;

import ecommerce.core.domain.brand.BrandCoreRequest;
import ecommerce.core.domain.brand.BrandCoreResponse;
import ecommerce.core.infra.BrandClientService;
import ecommerce.core.service.BrandService;
import ecommerce.core.service.validation.BrandValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandClientService brandClientService;
    private final BrandValidationService brandValidationService;

    @Override
    public BrandCoreResponse createBrand(BrandCoreRequest request) {
        log.info("Create brand at BrandServiceImpl");
        validateCreateRequest(request);
        return brandClientService.createBrand(request);
    }

    @Override
    public List<BrandCoreResponse> getAllBrands() {
        log.info("Get all brands at BrandServiceImpl");
        return brandClientService.getAllBrands();
    }

    @Override
    public BrandCoreResponse getBrandById(Long id) {
        log.info("Get brand by ID at BrandServiceImpl: {}", id);
        brandValidationService.validateBrandExists(id);
        return brandClientService.getBrandById(id);
    }

    @Override
    public BrandCoreResponse updateBrand(Long id, BrandCoreRequest request) {
        log.info("Update brand at BrandServiceImpl: {}", id);
        validateUpdateRequest(id, request);
        return brandClientService.updateBrand(id, request);
    }

    @Override
    public BrandCoreResponse deleteBrand(Long id) {
        log.info("Delete brand at BrandServiceImpl: {}", id);
        brandValidationService.validateBrandExists(id);
        return brandClientService.deleteBrand(id);
    }

    private void validateCreateRequest(BrandCoreRequest request) {
        log.info("Start validateCreateRequest for Brand");

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand name is required");
        }
        if (request.getSlug() == null || request.getSlug().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand slug is required");
        }

        log.info("End validateCreateRequest for Brand");
    }

    private void validateUpdateRequest(Long id, BrandCoreRequest request) {
        log.info("Start validateUpdateRequest for Brand");

        brandValidationService.validateBrandExists(id);

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand name is required");
        }
        if (request.getSlug() == null || request.getSlug().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand slug is required");
        }

        log.info("End validateUpdateRequest for Brand");
    }
}
