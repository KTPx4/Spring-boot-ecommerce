package ecommerce.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryApiResponse {
    private Long id;
    private String name;
    private String slug;
    private Long parent_id;
    private List<CategoryApiResponse> children;
}
