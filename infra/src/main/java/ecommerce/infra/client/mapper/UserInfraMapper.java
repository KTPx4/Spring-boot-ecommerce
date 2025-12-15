package ecommerce.infra.client.mapper;

import ecommerce.core.domain.user.UserCoreRequest;
import ecommerce.core.domain.user.UserCoreResponse;
import ecommerce.infra.client.entity.User;

import java.util.stream.Collectors;

public class UserInfraMapper {

    public static User toEntity(UserCoreRequest request, String encodedPassword) {
        User user = new User();
        user.setUserName(request.getUsername());
        user.setPassHash(encodedPassword);
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setBirthDay(request.getBirthDay());
        user.setGender(request.getGender());
        user.setStatus(1); // Active by default
        return user;
    }

    public static User toUpdateEntity(Long id, UserCoreRequest request, String encodedPassword) {
        User user = new User();
        user.setId(id);
        user.setUserName(request.getUsername());
        if (encodedPassword != null) {
            user.setPassHash(encodedPassword);
        }
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setBirthDay(request.getBirthDay());
        user.setGender(request.getGender());
        return user;
    }

    public static UserCoreResponse toUserCoreResponse(User user) {
        return UserCoreResponse.builder()
                .id(user.getId())
                .username(user.getUserName())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .birthDay(user.getBirthDay())
                .imgUrl(user.getImgUrl())
                .gender(user.getGender())
                .status(user.getStatus())
                .roles(user.getUserRoles().stream()
                        .map(ur -> ur.getRole().getName())
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
