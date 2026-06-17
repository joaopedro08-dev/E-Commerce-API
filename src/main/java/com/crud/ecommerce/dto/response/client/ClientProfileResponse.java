package com.crud.ecommerce.dto.response.client;

import com.crud.ecommerce.infrastructure.entity.client.Address;

public record ClientProfileResponse(
        String cpf,
        String rg,
        Address address
) {}