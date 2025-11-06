package ecommerce.core.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Set<String> roles;
    private Integer status;
}
