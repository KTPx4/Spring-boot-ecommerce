package ecommerce.infra.client.product.entity;

import ecommerce.infra.client.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_discounts")
public class ProductDiscount extends BaseEntity {
    private LocalDateTime from_date;
    private LocalDateTime to_date;
    private boolean is_valid;
    private float percent_discount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant product_variant;
}
