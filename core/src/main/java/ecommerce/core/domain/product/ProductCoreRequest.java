package ecommerce.core.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCoreRequest {
    private String name;
    private Long brandId;
    private Long categoryId;
    private Double price;
    private String description;
    private String sku;
    private String slug;
    private String shortDesc;
    private String imageUrl;

}
