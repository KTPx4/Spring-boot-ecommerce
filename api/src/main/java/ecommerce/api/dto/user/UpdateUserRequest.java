package ecommerce.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String email;
    private String fullName;
    private String phone;
    private String birthDay;
    private String gender;
    private String imgUrl;
    private String currentPassword; // Required if changing password
    private String newPassword; // Optional - only if user wants to change password
}
