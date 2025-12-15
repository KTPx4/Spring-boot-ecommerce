package ecommerce.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecommerce.api.dto.image.ImageUploadResponse;
import ecommerce.api.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "Image Upload", description = "Image upload and management APIs")
public class ImageUploadController {

    @Value("${cdn.url:http://localhost:3000/api/v1}")
    private String cdnUrl;

    @Value("${cdn.api-key:your_api_key_here}")
    private String cdnApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/upload")
    @Operation(summary = "Upload Single Image", description = "Upload a single image file to CDN. Supports jpg, png, gif, webp formats. Returns full image URL and thumbnails.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file format or empty file"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "500", description = "CDN server error")
    })
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "Image file to upload", required = true) @RequestParam("image") MultipartFile file,
            @Parameter(description = "Image category (e.g., product, user, brand)", example = "product") @RequestParam(value = "category", defaultValue = "general") String category,
            @Parameter(description = "Alt text for the image") @RequestParam(value = "alt", required = false) String alt) {

        log.info("Upload image request - file: {}, category: {}", file.getOriginalFilename(), category);

        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            if (!isImageFile(file)) {
                throw new IllegalArgumentException("File must be an image");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("x-api-key", cdnApiKey);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            body.add("category", category);
            if (alt != null) {
                body.add("alt", alt);
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String uploadUrl = cdnUrl + "/images/upload";
            ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            JsonNode dataNode = jsonResponse.get("data");

            ImageUploadResponse imageResponse = ImageUploadResponse.builder()
                    .id(dataNode.get("id").asText())
                    .path(dataNode.get("path").asText())
                    .url(dataNode.get("url").asText())
                    .category(dataNode.get("category").asText())
                    .uploadedAt(dataNode.get("uploadedAt").asText())
                    .build();

            if (dataNode.has("thumbnails")) {
                JsonNode thumbs = dataNode.get("thumbnails");
                imageResponse.setThumbnails(ImageUploadResponse.ImageThumbnails.builder()
                        .small(thumbs.get("small").asText())
                        .medium(thumbs.get("medium").asText())
                        .large(thumbs.get("large").asText())
                        .build());
            }

            if (dataNode.has("metadata")) {
                JsonNode meta = dataNode.get("metadata");
                imageResponse.setMetadata(ImageUploadResponse.ImageMetadata.builder()
                        .originalName(meta.get("originalName").asText())
                        .mimeType(meta.get("mimeType").asText())
                        .size(meta.get("size").asLong())
                        .width(meta.get("width").asInt())
                        .height(meta.get("height").asInt())
                        .format(meta.get("format").asText())
                        .build());
            }

            return ResponseBuilder.success("Image uploaded successfully", imageResponse);

        } catch (Exception e) {
            log.error("Error uploading image: ", e);
            return ResponseBuilder.error("Failed to upload image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-multiple")
    @Operation(summary = "Upload Multiple Images", description = "Upload up to 10 images at once to CDN. Returns array of image URLs and metadata.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Too many files (max 10) or invalid format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "500", description = "CDN server error")
    })
    public ResponseEntity<?> uploadMultipleImages(
            @Parameter(description = "Array of image files to upload (max 10)", required = true) @RequestParam("images") MultipartFile[] files,
            @Parameter(description = "Image category for all files", example = "product") @RequestParam(value = "category", defaultValue = "general") String category) {

        log.info("Upload multiple images request - count: {}, category: {}", files.length, category);

        try {
            if (files.length == 0) {
                throw new IllegalArgumentException("No files provided");
            }

            if (files.length > 10) {
                throw new IllegalArgumentException("Maximum 10 files allowed");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("x-api-key", cdnApiKey);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty() && isImageFile(file)) {
                    body.add("images",
                            new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
                }
            }
            body.add("category", category);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String uploadUrl = cdnUrl + "/images/upload-multiple";
            ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            return ResponseBuilder.success("Images uploaded successfully", jsonResponse.get("data"));

        } catch (Exception e) {
            log.error("Error uploading images: ", e);
            return ResponseBuilder.error("Failed to upload images: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{path}")
    @Operation(summary = "Delete Image", description = "Delete an image from CDN by its path. Requires ADMIN role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
            @ApiResponse(responseCode = "500", description = "CDN server error")
    })
    public ResponseEntity<?> deleteImage(
            @Parameter(description = "Image path/ID from CDN", required = true, example = "product/abc123.jpg") @PathVariable String path) {
        log.info("Delete image request - path: {}", path);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", cdnApiKey);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            String deleteUrl = cdnUrl + "/images/" + path;
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, String.class);

            return ResponseBuilder.success("Image deleted successfully", null);

        } catch (Exception e) {
            log.error("Error deleting image: ", e);
            return ResponseBuilder.error("Failed to delete image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    // Helper class for multipart file upload
    private static class MultipartInputStreamFileResource extends org.springframework.core.io.InputStreamResource {
        private final String filename;

        public MultipartInputStreamFileResource(java.io.InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() {
            return -1; // Unknown length
        }
    }
}
