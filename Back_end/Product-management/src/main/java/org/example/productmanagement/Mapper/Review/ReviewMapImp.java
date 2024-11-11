package org.example.productmanagement.Mapper.Review;


import org.example.productmanagement.DTO.Review.ReviewReponseDTO;
import org.example.productmanagement.DTO.Review.ReviewRequistDTO;
import org.example.productmanagement.Entites.Review;
import org.example.productmanagement.Model.User;
import org.example.productmanagement.Repository.ProductRepository;
import org.example.productmanagement.Web.UserControllers.UserOpenFeginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component// Marks this class as a Spring-managed component
public class ReviewMapImp implements ReviewMapInter {

	// Repositories and controllers needed for mapping
	@Autowired
	ProductRepository productRepository;// For retrieving product details
	@Autowired
	UserOpenFeginController userOpenFeginController;// For fetching user details


	/**
	 * Converts a ReviewRequistDTO to a Review entity.
	 *
	 * @param reviewRequistDTO the DTO containing review data
	 * @return a Review entity populated with the data from the DTO
	 */
	@Override
	public Review FromRequist2Review(ReviewRequistDTO reviewRequistDTO) {
		Review review = new Review();
		// Set fields from the DTO to the Review entity
		review.setIduser(reviewRequistDTO.getIduser());
		review.setIdProducts(reviewRequistDTO.getIdproduct());
		review.setComment(reviewRequistDTO.getComment());
		review.setRating(reviewRequistDTO.getRating());
		review.setDate(reviewRequistDTO.getDate());
		return review;
	}

	/**
	 * Converts a Review entity to a ReviewReponseDTO.
	 *
	 * @param review the Review entity to be converted
	 * @return a ReviewReponseDTO populated with the review and associated user data
	 */
	@Override
	public ReviewReponseDTO FromReview2Reponse(Review review) {
		ReviewReponseDTO reviewReponseDTO = new ReviewReponseDTO();
		// Fetch the product associated with the review
		reviewReponseDTO.setProduct(productRepository.findById(review.getIdProducts()).get());
		// Fetch the user associated with the review
		User user = userOpenFeginController.findOneUser(review.getIduser());

		// Set fields from the Review entity and User to the response DTO
		reviewReponseDTO.setId(review.getId());
		reviewReponseDTO.setUser(user);
		reviewReponseDTO.setComment(review.getComment());
		reviewReponseDTO.setRating(review.getRating());
		reviewReponseDTO.setDate(review.getDate());
		return reviewReponseDTO;
	}
}
