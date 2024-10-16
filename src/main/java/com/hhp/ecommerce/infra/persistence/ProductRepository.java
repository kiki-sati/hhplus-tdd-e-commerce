package com.hhp.ecommerce.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhp.ecommerce.domain.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
