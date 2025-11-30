package ecommerce.api.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponse {
    private String id;
    private String path;
    private String url;
    private ImageThumbnails thumbnails;
    private ImageMetadata metadata;
    private String category;
    private String uploadedAt;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageThumbnails {
        private String small;
        private String medium;
        private String large;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageMetadata {
        private String originalName;
        private String mimeType;
        private Long size;
        private Integer width;
        private Integer height;
        private String format;
    }
}
