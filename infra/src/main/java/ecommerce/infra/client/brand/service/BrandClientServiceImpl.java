package ecommerce.infra.client.brand.service;

import ecommerce.core.infra.BrandClientService;
import ecommerce.infra.client.product.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
