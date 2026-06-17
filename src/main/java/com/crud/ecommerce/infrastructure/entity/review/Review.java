package com.crud.ecommerce.infrastructure.entity.review;

import com.crud.ecommerce.infrastructure.entity.BaseEntity;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_reviews")
@Getter
@Setter
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;

    @Column(nullable = false, name = "rating", length = 1)
    private Integer rating;

    @Column(nullable = false, name = "title", length = 100)
    private String title;

    @Column(nullable = false, name = "comment", length = 1000)
    private String comment;
}