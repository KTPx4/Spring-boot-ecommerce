package com.main.ecommerce.model.products;

import com.main.ecommerce.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity {


    @ManyToOne(fetch = FetchType.EAGER)
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
}
