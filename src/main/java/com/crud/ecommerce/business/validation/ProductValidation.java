package com.crud.ecommerce.business.validation;

import com.crud.ecommerce.business.constants.ValidationConstants;
import com.crud.ecommerce.dto.resquest.product.ProductCreateInput;
import com.crud.ecommerce.dto.resquest.product.ProductUpdateInput;
import com.crud.ecommerce.business.util.ValidationUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductValidation {

    public void validateCreate(ProductCreateInput input) {
        List<String> errors = new ArrayList<>();

        ValidationUtils.validateRequiredInput(input);
        validateCreateFields(input, errors);
        ValidationUtils.validateErrors(errors);
    }

    public void validateUpdate(ProductUpdateInput input) {
        List<String> errors = new ArrayList<>();

        ValidationUtils.validateRequiredInput(input);
        ValidationUtils.validateAtLeastOneField(input.name(), input.description(),
                        input.price(), input.stock());
        validateUpdateFields(input, errors);
        ValidationUtils.validateErrors(errors);
    }

    private void validateCreateFields(ProductCreateInput input, List<String> errors) {
        validateName(input.name(), errors);
        validateDescription(input.description(), errors);
        validatePrice(input.price(), errors);
        validateStock(input.stock(), errors);
    }

    private void validateUpdateFields(ProductUpdateInput input, List<String> errors) {
        if (input.name() != null) validateName(input.name(), errors);
        if (input.description() != null) validateDescription(input.description(), errors);
        if (input.price() != null) validatePrice(input.price(), errors);
        if (input.stock() != null) validateStock(input.stock(), errors);
    }

    private void validateName(String nameValue, List<String> errors) {
        String name = ValidationUtils.trimOrNull(nameValue);

        if ((name == null) || (!ValidationConstants.Product.NAME_PATTERN.matcher(name).matches()))
            errors.add("Nome inválido: use apenas letras, números e espaços (3 a 120 caracteres).");
    }

    private void validateDescription(String descriptionValue, List<String> errors) {
        String description = ValidationUtils.trimOrNull(descriptionValue);

        if ((description == null) ||
                (!ValidationConstants.Product.DESCRIPTION_PATTERN.matcher(description).matches()))
            errors.add("Descrição inválida: deve possuir entre 20 e 500 caracteres.");
    }

    private void validatePrice(BigDecimal price, List<String> errors) {
        if ((price == null) || (price.compareTo(BigDecimal.ZERO) <= 0) ||
                (price.compareTo(ValidationConstants.Product.PRICE_MAX_LIMIT) > 0))
            errors.add("Preço inválido: informe um valor entre 0,01 e 10.000.");
    }

    private void validateStock(Integer stock, List<String> errors) {
        if ((stock == null) || (stock < 0) || (stock > ValidationConstants.Product.STOCK_MAX_LIMIT))
            errors.add("Estoque inválido: informe um valor entre 0 e 10.000.");
    }
}