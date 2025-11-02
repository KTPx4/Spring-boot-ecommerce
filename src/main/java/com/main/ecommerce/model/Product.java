package com.main.ecommerce.model;

import com.main.ecommerce.dto.ProductDTO;
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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double price;
    private String description;
    private String sku;
    private String slug;
    private int brandId;
    private String desc;
    private String shortDesc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;

    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.description = productDTO.getDescription();
        this.sku = productDTO.getSku();
        this.slug = productDTO.getSlug();
        this.brandId = productDTO.getBrandId();
        this.desc = productDTO.getDesc();
        this.shortDesc = productDTO.getShortDesc();
        this.imageUrl = productDTO.getImageUrl();

    }

}
