package com.shaurya.JobApplication.review.impl;

import com.shaurya.JobApplication.company.Company;
import com.shaurya.JobApplication.company.CompanyService;
import com.shaurya.JobApplication.review.Review;
import com.shaurya.JobApplication.review.ReviewRepository;
import com.shaurya.JobApplication.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        Company company = companyService.getCompanyById(companyId);
        if(company != null){
            review.setCompany(company);
            reviewRepository.save(review);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Review getReview(Long companyId, Long reviewId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);

        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review updatedReview) {
        Company company = companyService.getCompanyById(companyId);
        if(company == null) return false;

        Review existingReview = reviewRepository.findById(reviewId).orElse(null);
        if (existingReview == null) return false;

        if(!existingReview.getCompany().getId().equals(companyId)) return false;



        existingReview.setTitle(updatedReview.getTitle());
        existingReview.setDescription(updatedReview.getDescription());
        existingReview.setRating(updatedReview.getRating());

        reviewRepository.save(existingReview);
        return true;
    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        Review existingReview = reviewRepository.findById(reviewId).orElse(null);
        if (existingReview == null) return false;

        if(!existingReview.getCompany().getId().equals(companyId)) return false;

        reviewRepository.delete(existingReview);
        return true;
    }
}
