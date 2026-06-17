package com.crud.ecommerce.dto.resquest.product;

import java.math.BigDecimal;

public record ProductUpdateInput(
        String name,
        String description,
        BigDecimal price,
        Integer stock
) {}