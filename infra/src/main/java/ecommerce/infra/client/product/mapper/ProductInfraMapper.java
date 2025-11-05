package ecommerce.infra.client.product.mapper;

import ecommerce.core.domain.product.ProductCoreRequest;
import ecommerce.core.domain.product.ProductCoreResponse;
import ecommerce.infra.client.product.entity.Brand;
import ecommerce.infra.client.product.entity.Category;
import ecommerce.infra.client.product.entity.Product;

public class ProductInfraMapper {
    public static Product toEntity(ProductCoreRequest productCoreRequest, Brand brand, Category category) {


        Product product = new Product();
        product.setName(productCoreRequest.getName());
        product.setDescription(productCoreRequest.getDescription());
        product.setPrice(productCoreRequest.getPrice());
        product.setSku(productCoreRequest.getSku());
        product.setSlug(productCoreRequest.getSlug());
        product.setImage_url(productCoreRequest.getImageUrl());
        product.setShort_desc(productCoreRequest.getShortDesc());

        product.setCategory(category);
        product.setBrand(brand);
        return product;
    }

    public static ProductCoreResponse toProductCoreResponse(Product product) {
        ProductCoreResponse productCoreResponse = new ProductCoreResponse();
        productCoreResponse.setId(product.getId());
        productCoreResponse.setName(product.getName());
        productCoreResponse.setDescription(product.getDescription());
        productCoreResponse.setPrice(product.getPrice());
        productCoreResponse.setSku(product.getSku());
        productCoreResponse.setSlug(product.getSlug());
        productCoreResponse.setImage_url(product.getImage_url());
        productCoreResponse.setShort_desc(product.getShort_desc());
        return productCoreResponse;
    }
}
