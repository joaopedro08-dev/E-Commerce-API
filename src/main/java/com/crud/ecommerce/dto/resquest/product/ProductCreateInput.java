package com.crud.ecommerce.dto.resquest.product;

import java.math.BigDecimal;

public record ProductCreateInput(
        String name,
        String description,
        BigDecimal price,
        Integer stock
) { }