package com.hhp.ecommerce.application.service;

import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.infra.persistence.ProductRepository;
import com.hhp.ecommerce.presentation.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
