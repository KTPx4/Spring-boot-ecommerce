package ecommerce.infra.client.product.entity;

import ecommerce.infra.client.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_specifications")
public class ProductSpecification extends BaseEntity {

    @OneToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
}
