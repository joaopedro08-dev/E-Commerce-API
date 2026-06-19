package com.crud.ecommerce.controller;

import com.crud.ecommerce.business.service.ReviewService;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.response.review.ReviewResponse;
import com.crud.ecommerce.dto.resquest.review.ReviewCreateInput;
import com.crud.ecommerce.dto.resquest.review.ReviewUpdateInput;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/list")
    public List<ReviewResponse> getAllReviews(@RequestParam(defaultValue = "title") String sortBy){
        return reviewService.getAllReviews(sortBy);
    }

    @GetMapping("/{id}")
    public ReviewResponse getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    public Response createReview(@RequestBody ReviewCreateInput input){
        return reviewService.createReview(input);
    }

    @PutMapping("/{id}")
    public Response updateReview(@PathVariable Long id, @RequestBody ReviewUpdateInput input) {
        return reviewService.updateReview(id, input);
    }

    @DeleteMapping("/{id}")
    public Response deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }
}