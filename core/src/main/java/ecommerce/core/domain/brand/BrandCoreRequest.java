package ecommerce.core.domain.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandCoreRequest {
    private String name;
    private String slug;
    private String logoUrl;
}
