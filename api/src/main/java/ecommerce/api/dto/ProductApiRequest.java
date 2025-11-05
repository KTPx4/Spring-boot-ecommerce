package ecommerce.api.dto;

public class ProductApiRequest {
    private String name;
    private Double price;
    private String description;
    private String sku;
    private String slug;
    private Long brand_id;
    private Long category_id;
    private String short_desc;
    private String image_url;

    public ProductApiRequest(String name, Double price, String description, String sku, String slug, Long brand_id, Long category_id, String short_desc, String image_url) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.sku = sku;
        this.slug = slug;
        this.brand_id = brand_id;
        this.category_id = category_id;
        this.short_desc = short_desc;
        this.image_url = image_url;
    }

    public ProductApiRequest() {
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
}
