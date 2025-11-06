package ecommerce.core.domain.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCoreRequest {
    private String name;
    private String slug;
    private Long parentId;
}
