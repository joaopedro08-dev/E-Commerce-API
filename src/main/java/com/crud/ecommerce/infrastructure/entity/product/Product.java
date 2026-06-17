package com.crud.ecommerce.infrastructure.entity.product;

import com.crud.ecommerce.infrastructure.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_products")
@Getter
@Setter
public class Product extends BaseEntity {

    @Column(nullable = false, name = "name", length = 120)
    private String name;

    @Column(nullable = false, name = "description", length = 500)
    private String description;

    @Column(nullable = false, name = "price", precision = 10, scale = 2, length = 4)
    private BigDecimal price;

    @Column(nullable = false, name = "stock", length = 4)
    private Integer stock;
}