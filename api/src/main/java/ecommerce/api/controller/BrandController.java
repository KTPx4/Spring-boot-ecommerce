package ecommerce.api.controller;

import ecommerce.api.dto.BrandApiRequest;
import ecommerce.api.mapper.BrandMapper;
import ecommerce.api.util.ResponseBuilder;
import ecommerce.core.domain.brand.BrandCoreRequest;
import ecommerce.core.domain.brand.BrandCoreResponse;
import ecommerce.core.service.BrandService;
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
@RequestMapping("/api/v1/brand")
@Tag(name = "Brand", description = "Brand management APIs - Public GET, Admin required for CUD operations")
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    // @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Create new brand", description = "Create a new brand. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    public ResponseEntity<?> createBrand(
            @Parameter(description = "Brand details", required = true) @RequestBody BrandApiRequest brand) {
        log.info("REST request to create Brand : {}", brand);
        BrandCoreRequest coreRequest = BrandMapper.toBrandCoreRequest(brand);
        BrandCoreResponse res = brandService.createBrand(coreRequest);
        return ResponseBuilder.success("Create brand success", res);
    }

    @GetMapping
    // @PermitAll
    @Operation(summary = "Get all brands", description = "Retrieve all brands. Public endpoint - No authentication required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brands retrieved successfully")
    })
    public ResponseEntity<?> getAllBrands() {
        log.info("REST request to get all Brands");
        List<BrandCoreResponse> res = brandService.getAllBrands();
        return ResponseBuilder.success("Get all brands success", res);
    }

    @GetMapping("/{id}")
    // @PermitAll
    @Operation(summary = "Get brand by ID", description = "Retrieve a brand by its ID. Public endpoint - No authentication required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand found"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<?> getBrandById(
            @Parameter(description = "Brand ID", required = true, example = "1") @PathVariable Long id) {
        log.info("REST request to get Brand by ID : {}", id);
        BrandCoreResponse res = brandService.getBrandById(id);
        return ResponseBuilder.success("Get brand success", res);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update brand", description = "Update an existing brand. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<?> updateBrand(
            @Parameter(description = "Brand ID", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "Updated brand details", required = true) @RequestBody BrandApiRequest brand) {
        log.info("REST request to update Brand : {}", id);
        BrandCoreRequest coreRequest = BrandMapper.toBrandCoreRequest(brand);
        BrandCoreResponse res = brandService.updateBrand(id, coreRequest);
        return ResponseBuilder.success("Update brand success", res);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete brand", description = "Delete a brand by ID. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<?> deleteBrand(
            @Parameter(description = "Brand ID", required = true, example = "1") @PathVariable Long id) {
        log.info("REST request to delete Brand : {}", id);
        BrandCoreResponse res = brandService.deleteBrand(id);
        return ResponseBuilder.success("Delete brand success", res);
    }
}
