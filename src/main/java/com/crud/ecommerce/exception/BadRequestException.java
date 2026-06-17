package com.crud.ecommerce.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {

    private final List<String> errors;

    public BadRequestException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    public BadRequestException(List<String> errors) {
        super(errors.getFirst());
        this.errors = errors;
    }
}
