package com.crud.ecommerce.business.mapper;

import com.crud.ecommerce.dto.response.review.ReviewResponse;
import com.crud.ecommerce.dto.resquest.review.ReviewCreateInput;
import com.crud.ecommerce.dto.resquest.review.ReviewUpdateInput;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import com.crud.ecommerce.infrastructure.entity.review.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewMapperTest {

    private ReviewMapper reviewMapper;

    @BeforeEach
    void setUp() {
        reviewMapper = new ReviewMapper();
    }

    private Product validProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setDescription("Descrição válida com mais de vinte caracteres aqui.");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStock(10);
        return product;
    }

    private Client validClient() {
        Client client = new Client();
        client.setId(1L);
        client.setFullName("Cliente Teste");
        client.setEmail("cliente@email.com");
        client.setPhone("(11) 99999-1234");
        return client;
    }

    private Review validReview() {
        Review review = new Review();
        review.setId(1L);
        review.setProduct(validProduct());
        review.setClient(validClient());
        review.setRating(5);
        review.setTitle("Produto excelente");
        review.setComment("Chegou rápido e a qualidade é ótima, recomendo a todos.");
        return review;
    }

    // ==================== toReviewResponse ====================

    @Test
    void shouldMapReviewToReviewResponse() {
        Review review = validReview();

        ReviewResponse response = reviewMapper.toReviewResponse(review);

        assertEquals(review.getId(), response.id());
        assertEquals(review.getRating(), response.rating());
        assertEquals(review.getTitle(), response.title());
        assertEquals(review.getComment(), response.comment());
    }

    @Test
    void shouldMapProductSummaryInsideReviewResponse() {
        Review review = validReview();

        ReviewResponse response = reviewMapper.toReviewResponse(review);

        assertNotNull(response.product());
        assertEquals(1L, response.product().id());
        assertEquals("Produto Teste", response.product().name());
    }

    @Test
    void shouldMapClientSummaryInsideReviewResponse() {
        Review review = validReview();

        ReviewResponse response = reviewMapper.toReviewResponse(review);

        assertNotNull(response.client());
        assertEquals(1L, response.client().id());
        assertEquals("Cliente Teste", response.client().name());
    }

    @Test
    void shouldReturnNullProductWhenReviewHasNoProduct() {
        Review review = validReview();
        review.setProduct(null);

        ReviewResponse response = reviewMapper.toReviewResponse(review);

        assertNull(response.product());
    }

    @Test
    void shouldReturnNullClientWhenReviewHasNoClient() {
        Review review = validReview();
        review.setClient(null);

        ReviewResponse response = reviewMapper.toReviewResponse(review);

        assertNull(response.client());
    }

    // ==================== toReviewResponseList ====================

    @Test
    void shouldMapReviewListToReviewResponseList() {
        Review review1 = validReview();
        Review review2 = validReview();
        review2.setId(2L);
        review2.setTitle("Outro título válido");

        List<ReviewResponse> responses = reviewMapper.toReviewResponseList(List.of(review1, review2));

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).id());
        assertEquals(2L, responses.get(1).id());
    }

    @Test
    void shouldReturnEmptyListWhenReviewListIsEmpty() {
        List<ReviewResponse> responses = reviewMapper.toReviewResponseList(List.of());

        assertTrue(responses.isEmpty());
    }

    // ==================== applyCreateInput ====================

    @Test
    void shouldApplyCreateInputToReview() {
        Review review = new Review();
        ReviewCreateInput input = new ReviewCreateInput(
                1L, 1L, 5, "Produto excelente", "Chegou rápido e a qualidade é ótima."
        );

        reviewMapper.applyCreateInput(review, input);

        assertEquals(5, review.getRating());
        assertEquals("Produto excelente", review.getTitle());
        assertEquals("Chegou rápido e a qualidade é ótima.", review.getComment());
    }

    // ==================== applyUpdateInput ====================

    @Test
    void shouldApplyUpdateInputOnlyToProvidedFields() {
        Review review = validReview();

        ReviewUpdateInput input = new ReviewUpdateInput(4, null, null);

        reviewMapper.applyUpdateInput(review, input);

        assertEquals(4, review.getRating());
        assertEquals("Produto excelente", review.getTitle());
        assertEquals("Chegou rápido e a qualidade é ótima, recomendo a todos.", review.getComment());
    }

    @Test
    void shouldNotChangeReviewWhenAllUpdateFieldsAreNull() {
        Review review = validReview();

        ReviewUpdateInput input = new ReviewUpdateInput(null, null, null);

        reviewMapper.applyUpdateInput(review, input);

        assertEquals(5, review.getRating());
        assertEquals("Produto excelente", review.getTitle());
    }

    @Test
    void shouldApplyAllUpdateFieldsWhenAllAreProvided() {
        Review review = validReview();

        ReviewUpdateInput input = new ReviewUpdateInput(
                3, "Título atualizado", "Comentário atualizado com mais de dez caracteres."
        );

        reviewMapper.applyUpdateInput(review, input);

        assertEquals(3, review.getRating());
        assertEquals("Título atualizado", review.getTitle());
        assertEquals("Comentário atualizado com mais de dez caracteres.", review.getComment());
    }
}