package com.crud.ecommerce.dto.response.review;

import com.crud.ecommerce.dto.response.client.ClientSummaryResponse;
import com.crud.ecommerce.dto.response.product.ProductSummaryResponse;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        ProductSummaryResponse product,
        ClientSummaryResponse client,
        Integer rating,
        String title,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}