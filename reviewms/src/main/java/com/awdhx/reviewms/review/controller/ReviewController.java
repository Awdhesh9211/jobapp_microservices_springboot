package com.awdhx.reviewms.review.controller;


import com.awdhx.reviewms.review.entity.Review;
import com.awdhx.reviewms.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

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
        reviewService.addReview(companyId,review);
        return new ResponseEntity<>("review added successfully!",HttpStatus.OK);
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




}
