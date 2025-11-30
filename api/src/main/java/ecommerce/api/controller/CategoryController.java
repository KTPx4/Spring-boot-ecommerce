package ecommerce.api.controller;

import ecommerce.api.dto.CategoryApiRequest;
import ecommerce.api.mapper.CategoryMapper;
import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.category.CategoryCoreRequest;
import ecommerce.core.domain.category.CategoryCoreResponse;
import ecommerce.core.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@Tag(name = "Category", description = "Category management APIs - Public GET, Admin required for CUD operations")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Create new category", description = "Create a new category. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    public ResponseEntity<?> createCategory(
            @Parameter(description = "Category details", required = true) @RequestBody CategoryApiRequest category) {
        log.info("REST request to create Category : {}", category);
        CategoryCoreRequest coreRequest = CategoryMapper.toCategoryCoreRequest(category);
        CategoryCoreResponse res = categoryService.createCategory(coreRequest);
        return ResponseBuilder.success("Create category success", res);
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get all categories", description = "Retrieve all categories. Public endpoint - No authentication required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<?> getAllCategories() {
        log.info("REST request to get all Categories");
        List<CategoryCoreResponse> res = categoryService.getAllCategories();
        return ResponseBuilder.success("Get all categories success", res);
    }

    @GetMapping("/{id}")
    @PermitAll
    @Operation(summary = "Get category by ID", description = "Retrieve a category by its ID. Public endpoint - No authentication required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<?> getCategoryById(
            @Parameter(description = "Category ID", required = true, example = "1") @PathVariable Long id) {
        log.info("REST request to get Category by ID : {}", id);
        CategoryCoreResponse res = categoryService.getCategoryById(id);
        return ResponseBuilder.success("Get category success", res);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update category", description = "Update an existing category. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<?> updateCategory(
            @Parameter(description = "Category ID", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Updated category details", required = true) @RequestBody CategoryApiRequest category) {
        log.info("REST request to update Category : {}", id);
        CategoryCoreRequest coreRequest = CategoryMapper.toCategoryCoreRequest(category);
        CategoryCoreResponse res = categoryService.updateCategory(id, coreRequest);
        return ResponseBuilder.success("Update category success", res);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete category", description = "Delete a category by ID. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<?> deleteCategory(
            @Parameter(description = "Category ID", required = true, example = "1") @PathVariable Long id) {
        log.info("REST request to delete Category : {}", id);
        CategoryCoreResponse res = categoryService.deleteCategory(id);
        return ResponseBuilder.success("Delete category success", res);
    }
}
