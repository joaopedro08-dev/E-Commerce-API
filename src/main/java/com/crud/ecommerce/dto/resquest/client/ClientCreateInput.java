package com.crud.ecommerce.dto.resquest.client;

public record ClientCreateInput(
        String name,
        String email,
        String phone,
        Boolean statusClient,
        String cpf,
        String rg,
        AddressCreateInput address
) {
    public ClientCreateInput {
        if (statusClient == null) statusClient = true;
    }
}