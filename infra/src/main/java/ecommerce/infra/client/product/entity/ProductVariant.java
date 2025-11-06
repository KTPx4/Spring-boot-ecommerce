package ecommerce.infra.client.product.entity;

import ecommerce.infra.client.entity.AttrValue;
import ecommerce.infra.client.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String sku;
    private String image_url;

    @Column(precision = 18, scale = 2)
    private BigDecimal origin_price;

    @Column(precision = 18, scale = 2)
    private BigDecimal price;

    @Column(precision = 18, scale = 2)
    private BigDecimal sale_price;

    private long quantity_sold;
    private String currency;

    @ManyToMany
    @JoinTable(
            name = "product_variant_value",
            joinColumns = @JoinColumn(name = "variant_id"),
            inverseJoinColumns = @JoinColumn(name = "option_value_id")
    )
    private List<AttrValue> option_values = new ArrayList<>();
}
