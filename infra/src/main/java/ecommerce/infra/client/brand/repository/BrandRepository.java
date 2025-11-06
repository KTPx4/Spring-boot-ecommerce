package ecommerce.infra.client.brand.repository;

import ecommerce.infra.client.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsBySlug(String slug);
}
