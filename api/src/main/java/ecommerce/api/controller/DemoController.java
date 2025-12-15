package ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@Tag(name = "Demo", description = "Demo APIs for testing authorization")
@SecurityRequirement(name = "Bearer Authentication")
public class DemoController {

    @GetMapping("/user")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "User endpoint", description = "Accessible by USER and ADMIN roles")
    public ResponseEntity<Map<String, Object>> userEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is user endpoint");
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin endpoint", description = "Accessible by ADMIN role only")
    public ResponseEntity<Map<String, Object>> adminEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is admin endpoint");
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    @Operation(summary = "Public endpoint", description = "Accessible by everyone without authentication")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is public endpoint - no authentication required");
        
        return ResponseEntity.ok(response);
    }
}
