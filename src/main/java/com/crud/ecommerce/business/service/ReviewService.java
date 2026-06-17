package com.crud.ecommerce.business.service;

import com.crud.ecommerce.business.mapper.ReviewMapper;
import com.crud.ecommerce.business.util.EntityFinderUtils;
import com.crud.ecommerce.business.util.EntityOperationUtils;
import com.crud.ecommerce.business.validation.ReviewValidation;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.response.review.ReviewResponse;
import com.crud.ecommerce.dto.resquest.review.ReviewCreateInput;
import com.crud.ecommerce.dto.resquest.review.ReviewUpdateInput;
import com.crud.ecommerce.infrastructure.entity.review.Review;
import com.crud.ecommerce.infrastructure.repository.client.ClientRepository;
import com.crud.ecommerce.infrastructure.repository.product.ProductRepository;
import com.crud.ecommerce.infrastructure.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewValidation reviewValidation;

    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews(String sortBy) {
        return reviewMapper.toReviewResponseList(
                reviewRepository.findAll(Sort.by(sortBy).ascending()));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        return reviewMapper.toReviewResponse(findReviewById(id));
    }

    @Transactional
    public Response createReview(ReviewCreateInput input) {
        reviewValidation.validateCreate(input);

        Review review = new Review();

        resolveAndBindRelations(input, review);
        reviewMapper.applyCreateInput(review, input);
        return EntityOperationUtils.create(reviewRepository, review, "Avaliação criada com sucesso");
    }

    @Transactional
    public Response updateReview(Long id, ReviewUpdateInput input) {
        Review review = findReviewById(id);

        reviewValidation.validateUpdate(input);
        reviewMapper.applyUpdateInput(review, input);
        return EntityOperationUtils.update(reviewRepository, review, "Avaliação atualizada com sucesso");
    }

    @Transactional
    public Response deleteReview(Long id) {
        return EntityOperationUtils.delete(reviewRepository, findReviewById(id), "Avaliação deletada com sucesso");
    }

    private Review findReviewById(Long id) {
        return EntityFinderUtils.findById(reviewRepository, id, "Avaliação não encontrada!");
    }

    private void resolveAndBindRelations(ReviewCreateInput input, Review review) {
        review.setProduct(EntityFinderUtils.findById(productRepository, input.productId(),
                "Produto não encontrado!"));
        review.setClient(EntityFinderUtils.findById(clientRepository, input.clientId(),
                "Cliente não encontrado!"));
    }
}