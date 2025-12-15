package ecommerce.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandApiResponse {
    private Long id;
    private String name;
    private String slug;
    private String logo_url;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
