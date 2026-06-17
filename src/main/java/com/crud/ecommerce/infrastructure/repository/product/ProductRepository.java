package com.crud.ecommerce.infrastructure.repository.product;

import com.crud.ecommerce.infrastructure.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}