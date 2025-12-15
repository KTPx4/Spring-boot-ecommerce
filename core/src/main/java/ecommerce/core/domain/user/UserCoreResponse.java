package ecommerce.core.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCoreResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String birthDay;
    private String imgUrl;
    private String gender;
    private Integer status;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
