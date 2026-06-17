package com.crud.ecommerce.infrastructure.repository.review;

import com.crud.ecommerce.infrastructure.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByClientId(Long clientId);
    boolean existsByProductId(Long productId);
}