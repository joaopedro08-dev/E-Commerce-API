package com.crud.ecommerce.business.validation;

import com.crud.ecommerce.business.constants.ValidationConstants;
import com.crud.ecommerce.business.util.ValidationUtils;
import com.crud.ecommerce.dto.resquest.client.*;
import com.crud.ecommerce.dto.resquest.interfaces.client.AddressField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientValidation {

    public void validateCreate(ClientCreateInput input) {
        List<String> errors = new ArrayList<>();

        ValidationUtils.validateRequiredInput(input);
        validateCreateFields(input, errors);
        ValidationUtils.validateErrors(errors);
    }

    public void validateUpdate(ClientUpdateInput input) {
        List<String> errors = new ArrayList<>();

        ValidationUtils.validateRequiredInput(input);
        ValidationUtils.validateAtLeastOneField(input.name(), input.email(),
                input.phone(), input.cpf(), input.rg(), input.address());
        validateUpdateFields(input, errors);
        ValidationUtils.validateErrors(errors);
    }

    private void validateCreateFields(ClientCreateInput input, List<String> errors) {
        validateFullName(input.name(), errors);
        validateEmail(input.email(), errors);
        validatePhone(input.phone(), errors);
        validateCpf(input.cpf(), errors);
        validateRg(input.rg(), errors);
        validateAddress(input.address(), errors);
    }

    private void validateUpdateFields(ClientUpdateInput input, List<String> errors) {
        if (input.name() != null) validateFullName(input.name(), errors);
        if (input.email() != null) validateEmail(input.email(), errors);
        if (input.phone() != null) validatePhone(input.phone(), errors);
        if (input.cpf() != null) validateCpf(input.cpf(), errors);
        if (input.rg() != null) validateRg(input.rg(), errors);
        if (input.address() != null) validateAddress(input.address(), errors);
    }

    private void validateFullName(String nameValue, List<String> errors) {
        String name = ValidationUtils.trimOrNull(nameValue);

        if ((name == null) || (!ValidationConstants.Client.FULL_NAME_PATTERN.matcher(name).matches()))
            errors.add("Nome inválido: use apenas letras e espaços (3 a 100 caracteres).");
    }

    private void validateEmail(String emailValue, List<String> errors) {
        String email = ValidationUtils.trimOrNull(emailValue);

        if ((email == null) || (!ValidationConstants.Client.EMAIL_PATTERN.matcher(email).matches()))
            errors.add("Email inválido.");
    }

    private void validatePhone(String phoneValue, List<String> errors) {
        String phone = ValidationUtils.trimOrNull(phoneValue);

        if ((phone == null) || (!ValidationConstants.Client.PHONE_PATTERN.matcher(phone).matches()))
            errors.add("Telefone inválido: use o formato (99) 99999-9999.");
    }

    private void validateCpf(String cpfValue, List<String> errors) {
        String cpf = ValidationUtils.trimOrNull(cpfValue);

        if ((cpf == null) || (!ValidationConstants.Client.CPF_PATTERN.matcher(cpf).matches()))
            errors.add("CPF inválido: use o formato 000.000.000-00.");
    }

    private void validateRg(String rgValue, List<String> errors) {
        String rg = ValidationUtils.trimOrNull(rgValue);

        if ((rg == null) || (!ValidationConstants.Client.RG_PATTERN.matcher(rg).matches()))
            errors.add("RG inválido: use o formato 00.000.000-0.");
    }

    private void validateAddress(AddressCreateInput address, List<String> errors) {
        if (address == null) {
            errors.add("Endereço é obrigatório.");
            return;
        }
        validateAddressFields(address, errors);
    }

    private void validateAddress(AddressUpdateInput address, List<String> errors) {
        if (address == null) return;
        validateAddressFields(address, errors);
    }

    private void validateStreet(AddressField input, List<String> errors) {
        if (ValidationUtils.trimOrNull(input.street()) == null)
            errors.add("Rua é obrigatória.");
    }

    private void validateNumber(AddressField input, List<String> errors) {
        String number = ValidationUtils.trimOrNull(input.number());

        if ((number != null) && (!ValidationConstants.Address.ADDRESS_NUMBER_PATTERN.matcher(number).matches()))
            errors.add("Número inválido: use apenas números e letras (ex: 123, 12A).");
    }

    private void validateNeighborhood(AddressField input, List<String> errors) {
        if (ValidationUtils.trimOrNull(input.neighborhood()) == null)
            errors.add("Bairro é obrigatório.");
    }

    private void validateCity(AddressField input, List<String> errors) {
        if (ValidationUtils.trimOrNull(input.city()) == null)
            errors.add("Cidade é obrigatória.");
    }

    private void validateState(AddressField input, List<String> errors) {
        String state = ValidationUtils.trimOrNull(input.state());

        if ((state == null) || (!ValidationConstants.Address.STATE_PATTERN.matcher(state).matches()))
            errors.add("Estado inválido: use a sigla com 2 letras (ex: SP).");
    }

    private void validateZipCode(AddressField input, List<String> errors) {
        String zipCode = ValidationUtils.trimOrNull(input.zipCode());

        if ((zipCode == null) || (!ValidationConstants.Address.ZIP_CODE_PATTERN.matcher(zipCode).matches()))
            errors.add("CEP inválido: use o formato 00000-000.");
    }

    private void validateAddressFields(AddressField input, List<String> errors) {
        validateStreet(input, errors);
        validateNumber(input, errors);
        validateNeighborhood(input, errors);
        validateCity(input, errors);
        validateState(input, errors);
        validateZipCode(input, errors);
    }
}