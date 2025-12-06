package com.awdhx.reviewms.review.impl;



import com.awdhx.reviewms.review.entity.Review;
import com.awdhx.reviewms.review.repository.ReviewRepository;
import com.awdhx.reviewms.review.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


//  GET COMPANIES REVIEW
    @Override
    public List<Review> getAllReviewsByCompanyId(Long comapnyId) {
       return reviewRepository.findByCompanyId(comapnyId);
    }

//  GET REVIEW
    @Override
    public Review getReviewById(Long reviewId){
         return reviewRepository.findById( reviewId)
            .orElse(null);
    }

//  CREATE REVIEW
    @Override
    public boolean addReview(Long companyId, Review review){
        if(companyId != null && review != null){
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }else{
            return false;
        }
    }


//  DELETE REVIEW
    @Override
    @Transactional
    public boolean deleteReviewById(Long reviewId){
        try {
            reviewRepository.deleteById(reviewId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//  UPDATE REVIEW
    @Override
    public boolean updatedReview(Long reviewId, Review updatedReview) {

        Review review = reviewRepository.findById(reviewId)
                .orElse(null);

        if (review != null) {

            Optional.ofNullable(updatedReview.getTitle())
                    .ifPresent(review::setTitle);

            Optional.ofNullable(updatedReview.getDescription())
                    .ifPresent(review::setDescription);

            Optional.ofNullable(updatedReview.getRating())
                    .ifPresent(review::setRating);

            // IMPORTANT: save the updated existing object
            reviewRepository.save(review);

            return true;
        }

        return false;
    }

}
