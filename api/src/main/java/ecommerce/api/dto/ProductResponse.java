package ecommerce.api.dto;

import java.time.LocalDateTime;

public class ProductResponse {
    private Long id;
    private Long brand_id;
    private Long category_id;


    private String name;
    private Double price;
    private String description;
    private String sku;
    private String slug;
    private String short_desc;
    private String image_url;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public ProductResponse() {
    }

    public ProductResponse(Long id, Long brand_id, Long category_id, String name, Double price, String description, String sku, String slug, String short_desc, String image_url, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.brand_id = brand_id;
        this.category_id = category_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.sku = sku;
        this.slug = slug;
        this.short_desc = short_desc;
        this.image_url = image_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
