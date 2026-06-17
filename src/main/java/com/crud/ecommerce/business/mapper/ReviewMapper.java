package com.crud.ecommerce.business.mapper;

import com.crud.ecommerce.dto.response.client.ClientSummaryResponse;
import com.crud.ecommerce.dto.response.product.ProductSummaryResponse;
import com.crud.ecommerce.dto.response.review.ReviewResponse;
import com.crud.ecommerce.dto.resquest.review.ReviewCreateInput;
import com.crud.ecommerce.dto.resquest.review.ReviewUpdateInput;
import com.crud.ecommerce.infrastructure.entity.client.Client;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import com.crud.ecommerce.infrastructure.entity.review.Review;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {

    public List<ReviewResponse> toReviewResponseList(List<Review> reviews) {
        return reviews.stream().map(this::toReviewResponse).toList();
    }

    public ReviewResponse toReviewResponse(Review review) {
        return new ReviewResponse(review.getId(), toProductSummary(review.getProduct()),
                toClientSummary(review.getClient()), review.getRating(), review.getTitle(),
                review.getComment(), review.getCreatedAt(), review.getUpdatedAt());
    }

    public void applyCreateInput(Review model, ReviewCreateInput input) {
        model.setRating(input.rating());
        model.setTitle(input.title());
        model.setComment(input.comment());
    }

    public void applyUpdateInput(Review review, ReviewUpdateInput input) {
        if (input.rating() != null) review.setRating(input.rating());
        if (input.title() != null) review.setTitle(input.title());
        if (input.comment() != null) review.setComment(input.comment());
    }

    private ProductSummaryResponse toProductSummary(Product product) {
        if (product == null) return null;
        return new ProductSummaryResponse(product.getId(), product.getName(), product.getPrice());
    }

    private ClientSummaryResponse toClientSummary(Client client) {
        if (client == null) return null;
        return new ClientSummaryResponse(client.getId(), client.getFullName());
    }
}
