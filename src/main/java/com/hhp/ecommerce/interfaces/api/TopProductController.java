package com.hhp.ecommerce.interfaces.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.interfaces.dto.TopProductResponse;

@RestController
@RequestMapping("/products/top")
public class TopProductController {

    @GetMapping
    public List<TopProductResponse> getTopProducts() {

        List<TopProductResponse> topProducts = new ArrayList<>();
        topProducts.add(new TopProductResponse(UUID.randomUUID(), "Product A", 150, 1));
        topProducts.add(new TopProductResponse(UUID.randomUUID(), "Product B", 120, 2));
        topProducts.add(new TopProductResponse(UUID.randomUUID(), "Product C", 100, 3));
        topProducts.add(new TopProductResponse(UUID.randomUUID(), "Product D", 80, 4));
        topProducts.add(new TopProductResponse(UUID.randomUUID(), "Product E", 50, 5));
        return topProducts;
    }
}