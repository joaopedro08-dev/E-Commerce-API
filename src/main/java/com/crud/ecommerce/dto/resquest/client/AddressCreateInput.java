package com.crud.ecommerce.dto.resquest.client;

import com.crud.ecommerce.dto.resquest.interfaces.client.AddressField;

public record AddressCreateInput(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) implements AddressField {}