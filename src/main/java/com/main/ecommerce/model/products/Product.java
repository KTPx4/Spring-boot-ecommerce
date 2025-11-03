package com.main.ecommerce.model.products;

import com.main.ecommerce.dto.ProductDTO;
import com.main.ecommerce.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private double price;
    private String description;
    private String sku;
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = true)
    private Brand brand;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    private String short_desc;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String image_url;

    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.description = productDTO.getDescription();
        this.sku = productDTO.getSku();
        this.slug = productDTO.getSlug();
        this.brand = productDTO.getBrand();
        this.short_desc = productDTO.getShortDesc();
        this.image_url = productDTO.getImageUrl();

    }

}
