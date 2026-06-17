package com.crud.ecommerce.business.service;

import com.crud.ecommerce.business.mapper.ReviewMapper;
import com.crud.ecommerce.business.validation.ReviewValidation;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.response.review.ReviewResponse;
import com.crud.ecommerce.dto.resquest.review.ReviewCreateInput;
import com.crud.ecommerce.dto.resquest.review.ReviewUpdateInput;
import com.crud.ecommerce.exception.NotFoundException;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import com.crud.ecommerce.infrastructure.entity.review.Review;
import com.crud.ecommerce.infrastructure.repository.client.ClientRepository;
import com.crud.ecommerce.infrastructure.repository.product.ProductRepository;
import com.crud.ecommerce.infrastructure.repository.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private ReviewValidation reviewValidation;

    @InjectMocks
    private ReviewService reviewService;

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

    private ReviewCreateInput validCreateInput() {
        return new ReviewCreateInput(
                1L, 1L, 5, "Produto excelente", "Chegou rápido e a qualidade é ótima, recomendo a todos."
        );
    }

    // ==================== getAllReviews ====================

    @Test
    void shouldReturnAllReviewsSorted() {
        List<Review> reviews = List.of(validReview());
        List<ReviewResponse> expectedResponses = List.of(
                new ReviewResponse(1L, null, null, 5, "Produto excelente",
                        "Chegou rápido e a qualidade é ótima.", null, null)
        );

        when(reviewRepository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(reviews);
        when(reviewMapper.toReviewResponseList(reviews)).thenReturn(expectedResponses);

        List<ReviewResponse> result = reviewService.getAllReviews("rating");

        assertEquals(expectedResponses, result);
    }

    // ==================== getReviewById ====================

    @Test
    void shouldReturnReviewByIdWhenFound() {
        Review review = validReview();
        ReviewResponse expectedResponse = new ReviewResponse(
                1L, null, null, 5, "Produto excelente", "Chegou rápido e a qualidade é ótima.", null, null
        );

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewMapper.toReviewResponse(review)).thenReturn(expectedResponse);

        ReviewResponse result = reviewService.getReviewById(1L);

        assertEquals(expectedResponse, result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenReviewDoesNotExist() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.getReviewById(99L));
    }

    // ==================== createReview ====================

    @Test
    void shouldCreateReviewSuccessfully() {
        ReviewCreateInput input = validCreateInput();

        doNothing().when(reviewValidation).validateCreate(input);
        when(productRepository.findById(1L)).thenReturn(Optional.of(validProduct()));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(validClient()));
        when(reviewRepository.save(any(Review.class))).thenReturn(validReview());

        Response response = reviewService.createReview(input);

        assertTrue(response.success());
        assertEquals("Avaliação criada com sucesso", response.message());
        verify(reviewValidation).validateCreate(input);
        verify(reviewMapper).applyCreateInput(any(Review.class), eq(input));
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProductDoesNotExistOnCreate() {
        ReviewCreateInput input = validCreateInput();

        doNothing().when(reviewValidation).validateCreate(input);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.createReview(input));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenClientDoesNotExistOnCreate() {
        ReviewCreateInput input = validCreateInput();

        doNothing().when(reviewValidation).validateCreate(input);
        when(productRepository.findById(1L)).thenReturn(Optional.of(validProduct()));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.createReview(input));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void shouldNotSaveReviewWhenValidationFails() {
        ReviewCreateInput input = validCreateInput();

        doThrow(new com.crud.ecommerce.exception.BadRequestException("Dados inválidos"))
                .when(reviewValidation).validateCreate(input);

        assertThrows(com.crud.ecommerce.exception.BadRequestException.class,
                () -> reviewService.createReview(input));

        verify(reviewRepository, never()).save(any(Review.class));
    }

    // ==================== updateReview ====================

    @Test
    void shouldUpdateReviewSuccessfully() {
        Review existingReview = validReview();
        ReviewUpdateInput input = new ReviewUpdateInput(4, null, null);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(existingReview);

        Response response = reviewService.updateReview(1L, input);

        assertTrue(response.success());
        assertEquals("Avaliação atualizada com sucesso", response.message());
        verify(reviewValidation).validateUpdate(input);
        verify(reviewMapper).applyUpdateInput(existingReview, input);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentReview() {
        ReviewUpdateInput input = new ReviewUpdateInput(4, null, null);

        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.updateReview(99L, input));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    // ==================== deleteReview ====================

    @Test
    void shouldDeleteReviewSuccessfully() {
        Review review = validReview();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Response response = reviewService.deleteReview(1L);

        assertTrue(response.success());
        assertEquals("Avaliação deletada com sucesso", response.message());
        verify(reviewRepository).delete(review);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentReview() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.deleteReview(99L));
        verify(reviewRepository, never()).delete(any(Review.class));
    }
}