package ecommerce.infra.client.service;

import ecommerce.core.domain.brand.BrandCoreRequest;
import ecommerce.core.domain.brand.BrandCoreResponse;
import ecommerce.core.infra.BrandClientService;
import ecommerce.infra.client.mapper.BrandInfraMapper;
import ecommerce.infra.client.entity.Brand;
import ecommerce.infra.client.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandClientServiceImpl implements BrandClientService {
    private final BrandRepository brandRepository;

    @Override
    public boolean existsById(Long brandId) {
        log.info("Checking if brand exists: {}", brandId);
        return brandRepository.existsById(brandId);
    }

    @Override
    public BrandCoreResponse createBrand(BrandCoreRequest request) {
        log.info("infra - start createBrand");
        Brand brand = BrandInfraMapper.toEntity(request);
        Brand savedBrand = brandRepository.save(brand);
        log.info("infra - end createBrand");
        return BrandInfraMapper.toBrandCoreResponse(savedBrand);
    }

    @Override
    public List<BrandCoreResponse> getAllBrands() {
        log.info("infra - start getAllBrands");
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(BrandInfraMapper::toBrandCoreResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BrandCoreResponse getBrandById(Long id) {
        log.info("infra - start getBrandById: {}", id);
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        return BrandInfraMapper.toBrandCoreResponse(brand);
    }

    @Override
    public BrandCoreResponse updateBrand(Long id, BrandCoreRequest request) {
        log.info("infra - start updateBrand: {}", id);

        // Check if exists
        brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

        Brand brand = BrandInfraMapper.toUpdateEntity(id, request);
        Brand updatedBrand = brandRepository.save(brand);

        log.info("infra - end updateBrand");
        return BrandInfraMapper.toBrandCoreResponse(updatedBrand);
    }

    @Override
    public BrandCoreResponse deleteBrand(Long id) {
        log.info("infra - start deleteBrand: {}", id);
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

        brandRepository.delete(brand);

        log.info("infra - end deleteBrand");
        return BrandInfraMapper.toBrandCoreResponse(brand);
    }

    @Override
    public boolean existsBySlug(String slug) {
        log.info("infra - checking if brand slug exists: {}", slug);
        return brandRepository.existsBySlug(slug);
    }
}
