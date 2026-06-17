package com.crud.ecommerce.dto.resquest.review;

public record ReviewUpdateInput(
        Integer rating,
        String title,
        String comment
) {}