package com.crud.ecommerce.infrastructure.entity.client;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Column(nullable = false, length = 150, name = "street")
    private String street;

    @Column(nullable = false, length = 10, name = "number")
    private String number;

    @Column(length = 100, name = "complement")
    private String complement;

    @Column(nullable = false, length = 100, name = "neighborhood")
    private String neighborhood;

    @Column(nullable = false, length = 100, name = "city")
    private String city;

    @Column(nullable = false, length = 2, name = "state")
    private String state;

    @Column(nullable = false, length = 9, name = "zip_code")
    private String zipCode;
}