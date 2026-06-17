package com.crud.ecommerce.dto.resquest.review;

public record ReviewCreateInput(
        Long productId,
        Long clientId,
        Integer rating,
        String title,
        String comment
) {}