package com.crud.ecommerce.infrastructure.config;

import com.crud.ecommerce.dto.response.ErrorResponse;
import com.crud.ecommerce.exception.BadRequestException;
import com.crud.ecommerce.exception.ConflictException;
import com.crud.ecommerce.exception.NotFoundException;
import com.crud.ecommerce.business.util.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, error.getMessage(), null, false, DateUtils.now()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, error.getMessage(), null, false, DateUtils.now()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException error) {
        List<String> errors = error.getErrors();

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Requisição inválida!", errors, false, DateUtils.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric() {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(500, "Erro interno do servidor", null, false, DateUtils.now()));
    }
}