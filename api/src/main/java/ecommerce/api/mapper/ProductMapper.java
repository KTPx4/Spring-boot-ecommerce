package ecommerce.api.mapper;

import ecommerce.api.dto.ProductApiRequest;
import ecommerce.core.domain.product.ProductCoreRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductMapper {
    public static ProductCoreRequest toProductCoreRequest(ProductApiRequest request)
    {
        log.info("THIS MAPPER RUN");
        ProductCoreRequest productCoreRequest = new ProductCoreRequest();
        productCoreRequest.setName(request.getName());
        productCoreRequest.setDescription(request.getDescription());
        productCoreRequest.setPrice(request.getPrice());
        productCoreRequest.setSku(request.getSku());
        productCoreRequest.setSlug(request.getSlug());
        productCoreRequest.setImageUrl(request.getImage_url());
        productCoreRequest.setCategoryId(request.getCategory_id());
        productCoreRequest.setBrandId(request.getBrand_id());
        return productCoreRequest;
    }

}
