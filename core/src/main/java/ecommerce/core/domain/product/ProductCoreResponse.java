package ecommerce.core.domain.product;

import lombok.Data;

@Data
public class ProductCoreResponse {
    private Long id;
    private String name;
    private Long brand_id;
    private Long category_id;
    private Double price;
    private String description;
    private String sku;
    private String slug;
    private String short_desc;
    private String image_url;
}
