package com.crud.ecommerce.business.validation;

import com.crud.ecommerce.dto.resquest.client.AddressCreateInput;
import com.crud.ecommerce.dto.resquest.client.AddressUpdateInput;
import com.crud.ecommerce.dto.resquest.client.ClientCreateInput;
import com.crud.ecommerce.dto.resquest.client.ClientUpdateInput;
import com.crud.ecommerce.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientValidationTest {

    private ClientValidation clientValidation;

    @BeforeEach
    void setUp() {
        clientValidation = new ClientValidation();
    }

    private AddressCreateInput validAddress() {
        return new AddressCreateInput(
                "Rua das Flores", "123", "Apto 45", "Centro",
                "São Paulo", "SP", "01310-100");
    }

    private ClientCreateInput validClientInput() {
        return new ClientCreateInput("João Pedro Silva", "joao.pedro@email.com",
                "(11) 99999-1234", true, "123.456.789-09",
                "12.345.678-9", validAddress());
    }

    // ==================== CREATE - NAME ====================

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        ClientCreateInput input = new ClientCreateInput(
                null, "joao@email.com", "(11) 99999-1234",
                true, "123.456.789-09", "12.345.678-9", validAddress()
        );
        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenNameHasNumbers() {
        ClientCreateInput input = new ClientCreateInput(
                "João123", "joao@email.com", "(11) 99999-1234",
                true, "123.456.789-09", "12.345.678-9", validAddress());

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    // ==================== CREATE - EMAIL ====================

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        ClientCreateInput input = new ClientCreateInput(
                "João Pedro", "email-invalido", "(11) 99999-1234",
                true, "123.456.789-09", "12.345.678-9", validAddress());

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    // ==================== CREATE - PHONE ====================

    @Test
    void shouldThrowExceptionWhenPhoneIsInvalid() {
        ClientCreateInput input = new ClientCreateInput(
                "João Pedro", "joao@email.com", "11999991234",
                true, "123.456.789-09", "12.345.678-9", validAddress());

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    // ==================== CREATE - CPF ====================

    @Test
    void shouldThrowExceptionWhenCpfIsInvalid() {
        ClientCreateInput input = new ClientCreateInput(
                "João Pedro", "joao@email.com", "(11) 99999-1234",
                true, "12345678909", "12.345.678-9", validAddress());

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    // ==================== CREATE - RG ====================

    @Test
    void shouldThrowExceptionWhenRgIsInvalid() {
        ClientCreateInput input = new ClientCreateInput(
                "João Pedro", "joao@email.com", "(11) 99999-1234",
                true, "123.456.789-09", "123456789", validAddress());

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    // ==================== CREATE - ADDRESS ====================

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        ClientCreateInput input = new ClientCreateInput(
                "João Pedro", "joao@email.com", "(11) 99999-1234",
                true, "123.456.789-09", "12.345.678-9", null);

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenAddressStateIsInvalid() {
        AddressCreateInput invalidAddress = new AddressCreateInput(
                "Rua das Flores", "123", "Apto 45",
                "Centro", "São Paulo", "SaoPaulo", "01310-100");

        ClientCreateInput input = new ClientCreateInput(
                "João Pedro", "joao@email.com", "(11) 99999-1234",
                true, "123.456.789-09", "12.345.678-9", invalidAddress);

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenZipCodeIsInvalid() {
        AddressCreateInput invalidAddress = new AddressCreateInput(
                "Rua das Flores", "123", "Apto 45",
                "Centro", "São Paulo", "SP", "01310100");

        ClientCreateInput input = new ClientCreateInput(
                "João Pedro", "joao@email.com", "(11) 99999-1234",
                true, "123.456.789-09", "12.345.678-9", invalidAddress);

        assertThrows(BadRequestException.class, () -> clientValidation.validateCreate(input));
    }

    // ==================== CREATE - VALID ====================

    @Test
    void shouldPassWhenAllCreateFieldsAreValid() {
        assertDoesNotThrow(() -> clientValidation.validateCreate(validClientInput()));
    }

    // ==================== UPDATE ====================

    @Test
    void shouldThrowExceptionWhenAllUpdateFieldsAreNull() {
        ClientUpdateInput input = new ClientUpdateInput(
                null, null, null, null, null, null, null);

        assertThrows(BadRequestException.class, () -> clientValidation.validateUpdate(input));
    }

    @Test
    void shouldPassWhenUpdateHasOnlyValidName() {
        ClientUpdateInput input = new ClientUpdateInput(
                "Maria Silva", null, null, null, null, null, null);

        assertDoesNotThrow(() -> clientValidation.validateUpdate(input));
    }

    @Test
    void shouldThrowExceptionWhenUpdateAddressZipCodeIsInvalid() {
        AddressUpdateInput invalidAddress = new AddressUpdateInput(
                "Rua das Flores", "123", "Apto 45",
                "Centro", "São Paulo", "SP", "00000000");

        ClientUpdateInput input = new ClientUpdateInput(
                null, null, null, null, null, null, invalidAddress);

        assertThrows(BadRequestException.class, () -> clientValidation.validateUpdate(input));
    }
}