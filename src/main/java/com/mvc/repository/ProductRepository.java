package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mvc.model.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
