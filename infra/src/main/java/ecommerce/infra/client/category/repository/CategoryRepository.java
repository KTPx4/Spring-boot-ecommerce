package ecommerce.infra.client.category.repository;

import ecommerce.infra.client.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long> {
    boolean existsBySlug(String slug);
}
