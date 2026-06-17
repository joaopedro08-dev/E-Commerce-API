package com.crud.ecommerce.business.validation;

import com.crud.ecommerce.dto.resquest.product.ProductCreateInput;
import com.crud.ecommerce.dto.resquest.product.ProductUpdateInput;
import com.crud.ecommerce.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    private ProductValidation productValidation;

    @BeforeEach
    void setUp() {
        productValidation = new ProductValidation();
    }

    private ProductCreateInput validCreateInput() {
        return new ProductCreateInput("Produto Válido", "Descrição válida com mais de vinte caracteres aqui.",
                BigDecimal.valueOf(100), 10);
    }

    private ProductUpdateInput validUpdateInput() {
        return new ProductUpdateInput("Produto Atualizado", null, null, null);
    }

    // ==================== CREATE - NAME ====================

    @Test
    void shouldThrowExceptionWhenNameIsTooShort() {
        ProductCreateInput input = new ProductCreateInput("Ab", validCreateInput().description(),
                validCreateInput().price(), validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        ProductCreateInput input = new ProductCreateInput(null, validCreateInput().description(),
                validCreateInput().price(), validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        ProductCreateInput input = new ProductCreateInput("   ", validCreateInput().description(),
                validCreateInput().price(), validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    // ==================== CREATE - DESCRIPTION ====================

    @Test
    void shouldThrowExceptionWhenDescriptionIsTooShort() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                "Curta", validCreateInput().price(), validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                null, validCreateInput().price(), validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    // ==================== CREATE - PRICE ====================

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                validCreateInput().description(), BigDecimal.ZERO, validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                validCreateInput().description(), BigDecimal.valueOf(-1), validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenPriceExceedsLimit() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                validCreateInput().description(), BigDecimal.valueOf(10001), validCreateInput().stock());

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldAcceptPriceAtLimit() {
        ProductCreateInput input = new ProductCreateInput(
                validCreateInput().name(), validCreateInput().description(),
                BigDecimal.valueOf(10000), validCreateInput().stock());

        assertDoesNotThrow(() -> productValidation.validateCreate(input));
    }

    // ==================== CREATE - STOCK ====================

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                validCreateInput().description(), validCreateInput().price(), -1);

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenStockExceedsLimit() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                validCreateInput().description(), validCreateInput().price(), 10001);

        assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));
    }

    @Test
    void shouldAcceptStockAtLimit() {
        ProductCreateInput input = new ProductCreateInput(validCreateInput().name(),
                validCreateInput().description(), validCreateInput().price(), 10000);

        assertDoesNotThrow(() -> productValidation.validateCreate(input));
    }

    // ==================== CREATE - VALID ====================

    @Test
    void shouldPassWhenAllCreateFieldsAreValid() {
        assertDoesNotThrow(() -> productValidation.validateCreate(validCreateInput()));
    }

    // ==================== UPDATE ====================

    @Test
    void shouldPassWhenUpdateHasAtLeastOneValidField() {
        assertDoesNotThrow(() -> productValidation.validateUpdate(validUpdateInput()));
    }

    @Test
    void shouldThrowExceptionWhenAllUpdateFieldsAreNull() {
        ProductUpdateInput input =
                new ProductUpdateInput(null, null, null, null);

        assertThrows(BadRequestException.class, () -> productValidation.validateUpdate(input));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNameIsTooShort() {
        ProductUpdateInput input =
                new ProductUpdateInput("Ab", null, null, null);

        assertThrows(BadRequestException.class, () -> productValidation.validateUpdate(input));
    }

    @Test
    void shouldThrowExceptionWhenUpdatePriceIsNegative() {
        ProductUpdateInput input = new ProductUpdateInput(
                        null, null, BigDecimal.valueOf(-1), null);

        assertThrows(BadRequestException.class, () -> productValidation.validateUpdate(input));
    }

    // ==================== MULTIPLE ERRORS ====================

    @Test
    void shouldThrowExceptionWhenMultipleFieldsAreInvalid() {
        ProductCreateInput input = new ProductCreateInput("", "", BigDecimal.ZERO, -1);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> productValidation.validateCreate(input));

        assertNotNull(exception.getMessage());
    }
}