package com.crud.ecommerce.dto.response.client;

import java.time.LocalDateTime;

public record ClientResponse(
        Long id,
        String name,
        String email,
        String phone,
        ClientProfileResponse profile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}