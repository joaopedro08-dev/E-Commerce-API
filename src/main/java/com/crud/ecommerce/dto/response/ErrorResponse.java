package com.crud.ecommerce.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        Integer status,
        String message,
        List<String> errors,
        Boolean success,
        LocalDateTime timestamp
) {}