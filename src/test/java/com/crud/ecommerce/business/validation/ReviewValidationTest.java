package com.crud.ecommerce.business.validation;

import com.crud.ecommerce.dto.resquest.review.ReviewCreateInput;
import com.crud.ecommerce.dto.resquest.review.ReviewUpdateInput;
import com.crud.ecommerce.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewValidationTest {

    private ReviewValidation reviewValidation;

    @BeforeEach
    void setUp() {
        reviewValidation = new ReviewValidation();
    }

    private ReviewCreateInput validReviewInput() {
        return new ReviewCreateInput(1L, 1L, 5, "Produto excelente",
                "Chegou rápido e a qualidade é ótima, recomendo a todos.");
    }

    // ==================== CREATE - PRODUCT/CLIENT ID ====================

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        ReviewCreateInput input = new ReviewCreateInput(
                null, 1L, 5, "Produto excelente", "Chegou rápido e a qualidade é ótima."
        );

        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenClientIdIsNull() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, null, 5, "Produto excelente", "Chegou rápido e a qualidade é ótima."
        );
        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    // ==================== CREATE - RATING ====================

    @Test
    void shouldThrowExceptionWhenRatingIsNull() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, null, "Produto excelente", "Chegou rápido e a qualidade é ótima."
        );

        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenRatingIsBelowMinimum() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, 0, "Produto excelente", "Chegou rápido e a qualidade é ótima."
        );

        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenRatingIsAboveMaximum() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, 6, "Produto excelente", "Chegou rápido e a qualidade é ótima."
        );

        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    // ==================== CREATE - TITLE ====================

    @Test
    void shouldThrowExceptionWhenTitleIsTooShort() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, 5, "Ok", "Chegou rápido e a qualidade é ótima."
        );

        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, 5, null, "Chegou rápido e a qualidade é ótima."
        );

        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    // ==================== CREATE - COMMENT ====================

    @Test
    void shouldThrowExceptionWhenCommentIsTooShort() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, 5, "Produto excelente", "Curto."
        );
        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    @Test
    void shouldThrowExceptionWhenCommentIsNull() {
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, 5, "Produto excelente", null);

        assertThrows(BadRequestException.class, () -> reviewValidation.validateCreate(input));
    }

    // ==================== CREATE - VALID ====================

    @Test
    void shouldPassWhenAllCreateFieldsAreValid() {
        assertDoesNotThrow(() -> reviewValidation.validateCreate(validReviewInput()));
    }

    // ==================== UPDATE ====================

    @Test
    void shouldThrowExceptionWhenAllUpdateFieldsAreNull() {
        ReviewUpdateInput input = new ReviewUpdateInput(null, null, null);

        assertThrows(BadRequestException.class, () -> reviewValidation.validateUpdate(input));
    }

    @Test
    void shouldPassWhenUpdateHasOnlyValidRating() {
        ReviewUpdateInput input = new ReviewUpdateInput(4, null, null);

        assertDoesNotThrow(() -> reviewValidation.validateUpdate(input));
    }

    @Test
    void shouldThrowExceptionWhenUpdateRatingIsAboveMaximum() {
        ReviewUpdateInput input = new ReviewUpdateInput(10, null, null);

        assertThrows(BadRequestException.class, () -> reviewValidation.validateUpdate(input));
    }

    @Test
    void shouldThrowExceptionWhenUpdateCommentIsTooShort() {
        ReviewUpdateInput input = new ReviewUpdateInput(null, null, "Curto.");

        assertThrows(BadRequestException.class, () -> reviewValidation.validateUpdate(input));
    }
}