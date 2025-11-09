package ecommerce.api.controller;

import ecommerce.api.dto.user.UpdateUserRequest;
import ecommerce.api.dto.user.UserResponse;
import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.user.UserCoreRequest;
import ecommerce.core.domain.user.UserCoreResponse;
import ecommerce.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User profile and account management APIs")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get Current User Profile", description = "Retrieve profile information for the authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
    })
    public ResponseEntity<?> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        log.info("Get profile request for user: {}", username);

        UserCoreResponse userCore = userService.getUserByUsername(username);

        UserResponse response = UserResponse.builder()
                .id(userCore.getId())
                .username(userCore.getUsername())
                .email(userCore.getEmail())
                .fullName(userCore.getFullName())
                .phone(userCore.getPhone())
                .birthDay(userCore.getBirthDay())
                .imgUrl(userCore.getImgUrl())
                .gender(userCore.getGender())
                .status(userCore.getStatus())
                .roles(userCore.getRoles())
                .createdAt(userCore.getCreatedAt())
                .updatedAt(userCore.getUpdatedAt())
                .build();

        return ResponseBuilder.success("Get profile success", response);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update Current User Profile", description = "Update profile information for authenticated user. To change password, provide both currentPassword and newPassword.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or current password incorrect"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
    })
    public ResponseEntity<?> updateCurrentUserProfile(
            @Parameter(description = "Updated profile data", required = true) @RequestBody UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        log.info("Update profile request for user: {}", username);

        // Map API request to Core request
        UserCoreRequest coreRequest = UserCoreRequest.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .birthDay(request.getBirthDay())
                .gender(request.getGender())
                .imgUrl(request.getImgUrl())
                .currentPassword(request.getCurrentPassword())
                .password(request.getNewPassword())
                .build();

        UserCoreResponse userCore = userService.updateCurrentUser(username, coreRequest);

        UserResponse response = UserResponse.builder()
                .id(userCore.getId())
                .username(userCore.getUsername())
                .email(userCore.getEmail())
                .fullName(userCore.getFullName())
                .phone(userCore.getPhone())
                .birthDay(userCore.getBirthDay())
                .imgUrl(userCore.getImgUrl())
                .gender(userCore.getGender())
                .status(userCore.getStatus())
                .roles(userCore.getRoles())
                .createdAt(userCore.getCreatedAt())
                .updatedAt(userCore.getUpdatedAt())
                .build();

        return ResponseBuilder.success("Update profile success", response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve user information by ID. Requires authentication.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserById(
            @Parameter(description = "User ID", required = true, example = "1") @PathVariable Long id) {
        log.info("Get user by ID request: {}", id);

        UserCoreResponse userCore = userService.getUserById(id);

        UserResponse response = UserResponse.builder()
                .id(userCore.getId())
                .username(userCore.getUsername())
                .email(userCore.getEmail())
                .fullName(userCore.getFullName())
                .phone(userCore.getPhone())
                .birthDay(userCore.getBirthDay())
                .imgUrl(userCore.getImgUrl())
                .gender(userCore.getGender())
                .status(userCore.getStatus())
                .roles(userCore.getRoles())
                .createdAt(userCore.getCreatedAt())
                .updatedAt(userCore.getUpdatedAt())
                .build();

        return ResponseBuilder.success("Get user success", response);
    }
}
