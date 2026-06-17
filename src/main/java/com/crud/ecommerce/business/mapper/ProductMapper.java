package com.crud.ecommerce.business.mapper;

import com.crud.ecommerce.dto.response.product.ProductResponse;
import com.crud.ecommerce.dto.resquest.product.ProductCreateInput;
import com.crud.ecommerce.dto.resquest.product.ProductUpdateInput;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public List<ProductResponse> toProductResponseList(List<Product> products) {
        return products.stream().map(this::toProductResponse).toList();
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock(), product.getCreatedAt(), product.getUpdatedAt());
    }

    public void applyCreateInput(Product model, ProductCreateInput input) {
        model.setName(input.name());
        model.setDescription(input.description());
        model.setPrice(input.price());
        model.setStock(input.stock());
    }

    public void applyUpdateInput(Product product, ProductUpdateInput input) {
        if (input.name() != null) product.setName(input.name());
        if (input.description() != null) product.setDescription(input.description());
        if (input.price() != null) product.setPrice(input.price());
        if (input.stock() != null) product.setStock(input.stock());
    }
}