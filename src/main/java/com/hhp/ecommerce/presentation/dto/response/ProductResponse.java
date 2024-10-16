package com.hhp.ecommerce.presentation.dto.response;

import com.hhp.ecommerce.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private int stock;

    public static ProductResponse fromProduct(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}
