package com.crud.ecommerce.business.validation;

import com.crud.ecommerce.business.constants.ValidationConstants;
import com.crud.ecommerce.business.util.ValidationUtils;
import com.crud.ecommerce.dto.resquest.review.ReviewCreateInput;
import com.crud.ecommerce.dto.resquest.review.ReviewUpdateInput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewValidation {

    public void validateCreate(ReviewCreateInput input) {
        List<String> errors = new ArrayList<>();

        ValidationUtils.validateRequiredInput(input);
        validateCreateFields(input, errors);
        validateForeignKeys(input, errors);
        ValidationUtils.validateErrors(errors);
    }

    public void validateUpdate(ReviewUpdateInput input) {
        List<String> errors = new ArrayList<>();

        ValidationUtils.validateRequiredInput(input);
        ValidationUtils.validateAtLeastOneField(input.rating(), input.title(), input.comment());
        validateUpdateFields(input, errors);
        ValidationUtils.validateErrors(errors);
    }

    private void validateForeignKeys(ReviewCreateInput input, List<String> errors) {
        if (input.productId() == null) errors.add("Produto é obrigatório.");
        if (input.clientId() == null) errors.add("Cliente é obrigatório.");
    }

    private void validateCreateFields(ReviewCreateInput input, List<String> errors) {
        validateRating(input.rating(), errors);
        validateTitle(input.title(), errors);
        validateComment(input.comment(), errors);
    }

    private void validateUpdateFields(ReviewUpdateInput input, List<String> errors) {
        if (input.rating() != null) validateRating(input.rating(), errors);
        if (input.title() != null) validateTitle(input.title(), errors);
        if (input.comment() != null) validateComment(input.comment(), errors);
    }

    private void validateRating(Integer rating, List<String> errors) {
        if (rating == null || rating < ValidationConstants.Review.RATING_MIN
                || rating > ValidationConstants.Review.RATING_MAX)
            errors.add("Avaliação inválida: informe um valor entre 1 e 5.");
    }

    private void validateTitle(String titleValue, List<String> errors) {
        String title = ValidationUtils.trimOrNull(titleValue);

        if (title == null || !ValidationConstants.Review.TITLE_PATTERN.matcher(title).matches())
            errors.add("Título inválido: use entre 3 e 100 caracteres.");
    }

    private void validateComment(String commentValue, List<String> errors) {
        String comment = ValidationUtils.trimOrNull(commentValue);

        if (comment == null || comment.length() < ValidationConstants.Review.COMMENT_MIN
                || comment.length() > ValidationConstants.Review.COMMENT_MAX)
            errors.add("Comentário inválido: deve ter entre 10 e 500 caracteres.");
    }
}