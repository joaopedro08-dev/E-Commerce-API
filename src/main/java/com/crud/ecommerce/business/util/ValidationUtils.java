package com.crud.ecommerce.business.util;

import com.crud.ecommerce.exception.BadRequestException;

import java.util.List;

final public class ValidationUtils {

    private ValidationUtils() {}

    public static String trimOrNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static void validateErrors(List<String> errors) {
        if (!errors.isEmpty()) throw new BadRequestException(errors);
    }

    public static void validateRequiredInput(Object input) {
        if (input == null) throw new BadRequestException("Dados não informados.");
    }

    public static void validateAtLeastOneField(Object... fields) {
        for (Object field : fields)
            if (field != null) return;

        throw new BadRequestException("Informe pelo menos um campo para atualização.");
    }
}