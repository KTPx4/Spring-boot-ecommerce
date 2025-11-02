package com.main.ecommerce.dto;

import com.main.ecommerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
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

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.sku = product.getSku();
        this.slug = product.getSlug();
        this.brandId = product.getBrandId();
        this.desc = product.getDesc();
        this.shortDesc = product.getShortDesc();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
        this.imageUrl = product.getImageUrl();
    }
}