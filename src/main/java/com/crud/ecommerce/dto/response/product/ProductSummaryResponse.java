package com.crud.ecommerce.dto.response.product;

import java.math.BigDecimal;

public record ProductSummaryResponse(
        Long id,
        String name,
        BigDecimal price
) {}
