package com.awdhx.reviewms.review.controller;


import com.awdhx.reviewms.review.entity.Review;
import com.awdhx.reviewms.review.messaging.ReviewMessageProducer;
import com.awdhx.reviewms.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {


    private ReviewService reviewService;
    private ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }

    //  GET COMPANIES REVIEWS BY COMPANY ID
    @GetMapping("")
    public ResponseEntity<List<Review>> getAllReviewsByCompanyId(@RequestParam Long companyId){
        return ResponseEntity.ok(reviewService.getAllReviewsByCompanyId(companyId));
    }

//  GET PERTICULAR REVIEW
    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//  ADD REVIEW
    @PostMapping("")
    public ResponseEntity<String> addReview(@RequestParam Long companyId, @RequestBody Review review) {
        boolean isReviewSaved=reviewService.addReview(companyId,review);
        System.out.println(review.getCompanyId());
        if(isReviewSaved){
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>("review added successfully!",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("review not saved !",HttpStatus.NOT_FOUND);
        }
    }


//  DELETE REVIEW
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReviewById(@PathVariable Long reviewId) {
        boolean isdelete = reviewService.deleteReviewById( reviewId);
        if (isdelete) {
            System.out.println("DEEEEDHHH");
            return new ResponseEntity<>("deleted success!", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//  UPDATE REVIEW
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReviewById(@PathVariable Long reviewId,@RequestBody Review review) {
        boolean isupdated = reviewService.updatedReview( reviewId,review);
        if (isupdated) {
            return new ResponseEntity<>("updated success!", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/averageRating")
    public  Double getAvarageRating(@RequestParam Long companyId){
        List<Review> reviewList=reviewService.getAllReviewsByCompanyId(companyId);
        return reviewList.stream()
                .mapToDouble(Review::getRating).average()
                .orElse(0.0);
    }




}
