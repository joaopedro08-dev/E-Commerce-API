package com.crud.ecommerce.dto.resquest.client;

public record ClientUpdateInput(
        String name,
        String email,
        String phone,
        Boolean statusClient,
        String cpf,
        String rg,
        AddressUpdateInput address
) {}