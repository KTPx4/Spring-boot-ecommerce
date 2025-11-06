package ecommerce.core.domain.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCoreResponse {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private List<CategoryCoreResponse> children;
}
