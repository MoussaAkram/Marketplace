package org.example.productmanagement.Service.Review;

import jakarta.transaction.Transactional;

import org.example.productmanagement.DTO.Review.ReviewReponseDTO;
import org.example.productmanagement.DTO.Review.ReviewRequistDTO;
import org.example.productmanagement.Entites.Review;
import org.example.productmanagement.Mapper.Review.ReviewMapInter;
import org.example.productmanagement.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service // Indicates that this class is a service component in the Spring framework
@Transactional // Marks the class as transactional for database operations
public class ReviewServImp implements ReviewServInter {
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ReviewMapInter reviewMapInter;

	// Adds a new review
	@Override
	public void ADD(ReviewRequistDTO reviewRequistDTO) {
		reviewRepository.save(reviewMapInter.FromRequist2Review(reviewRequistDTO));
		// Convert DTO to Review entity and save it
	}

	// Retrieves a single review by its ID
	@Override
	public ReviewReponseDTO Getone(Long id) {
		// Fetch the review entity and convert it to a DTO
		Review review = reviewRepository.findById(id).get();
		return reviewMapInter.FromReview2Reponse(review);
	}

	// Retrieves a review for a specific user and product
	@Override
	public ReviewReponseDTO Getforuser(Long product, Long userid) {
		// Find the review by product ID and user ID
		Review review = reviewRepository.findByIdProductsAndIduser(product, userid);
		// Convert the found review to a response DTO
		ReviewReponseDTO reviewReponseDTO = reviewMapInter.FromReview2Reponse(review);
		return reviewReponseDTO;
	}

	// Retrieves all reviews for a specific product
	@Override
	public List<ReviewReponseDTO> Getall(Long idproduct) {
		// Fetch all reviews for the given product ID
		List<Review> reviews = reviewRepository.findByIdProducts(idproduct);
		List<ReviewReponseDTO> reviewReponseDTOS = new ArrayList<>();
		// Convert each Review entity to a ReviewReponseDTO
		for (Review review : reviews) {
			reviewReponseDTOS.add(reviewMapInter.FromReview2Reponse(review));
		}
		// Return the list of response DTOs
		return reviewReponseDTOS;
	}

	// Deletes a review by its ID
	@Override
	public void delete(Long id) {
		reviewRepository.deleteById(id);
	}
}
