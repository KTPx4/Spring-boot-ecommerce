package com.main.ecommerce.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
// ✅ Gom nhóm tất cả API trong controller này vào mục "Product APIs"
@Tag(name = "Product APIs", description = "Các API dùng để quản lý sản phẩm")
public class TestSwaggerController {

    @Operation(summary = "Lấy thông tin sản phẩm theo ID", description = "Trả về chi tiết một sản phẩm dựa trên ID được cung cấp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy sản phẩm thành công",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sản phẩm với ID này",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID của sản phẩm cần tìm", required = true, example = "123")
            @PathVariable String id) {
        // Logic để tìm sản phẩm...
        ProductDTO product = new ProductDTO("123", "Laptop ABC", 25000000);
        return ResponseEntity.ok(product);
    }
}

// Đây là class DTO (Data Transfer Object)
@Schema(description = "Model chứa thông tin chi tiết của sản phẩm")
class ProductDTO {

    @Schema(description = "ID định danh duy nhất của sản phẩm", example = "PROD-001")
    public String id;

    @Schema(description = "Tên của sản phẩm", example = "Laptop Gaming XYZ")
    public String name;

    @Schema(description = "Giá của sản phẩm, đơn vị VND", example = "30000000")
    public double price;

    // Constructors, getters, setters...
    public ProductDTO(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}