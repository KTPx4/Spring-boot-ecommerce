package ecommerce.core.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCoreRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String birthDay;
    private String gender;
    private String imgUrl;
    private String currentPassword; // For password change verification
}
