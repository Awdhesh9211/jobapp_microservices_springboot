package com.awdhx.reviewms.review.service;


import com.awdhx.reviewms.review.entity.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviewsByCompanyId(Long companyId);

    boolean addReview(Long companyId, Review review);

    Review getReviewById( Long reviewId);

    boolean deleteReviewById( Long reviewId);

    boolean updatedReview(Long reviewId, Review updatedReview);
}
