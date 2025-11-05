package ecommerce.api.mapper;

import ecommerce.api.dto.ProductApiRequest;
import ecommerce.core.domain.product.ProductCoreRequest;

public class ProductMapper {
    public static ProductCoreRequest toProductCoreRequest(ProductApiRequest request)
    {
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
