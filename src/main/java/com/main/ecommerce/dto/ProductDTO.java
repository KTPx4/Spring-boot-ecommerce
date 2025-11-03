package com.main.ecommerce.dto;

import com.main.ecommerce.model.products.Brand;
import com.main.ecommerce.model.products.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private long id;
    private String name;
    private double price;
    private String description;
    private String sku;
    private String slug;
    private Brand brand;
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
        this.brand = product.getBrand();
        this.shortDesc = product.getShort_desc();
        this.createdAt = product.getCreated_at();
        this.updatedAt = product.getUpdated_at();
        this.imageUrl = product.getImage_url();
    }
}