package ecommerce.infra.client.repository;

import ecommerce.infra.client.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySlug(String slug);
    boolean existsBySku(String sku);
}
