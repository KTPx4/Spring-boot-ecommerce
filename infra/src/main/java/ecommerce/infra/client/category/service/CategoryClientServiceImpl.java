package ecommerce.infra.client.category.service;

import ecommerce.core.infra.CategoryClientService;
import ecommerce.infra.client.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
