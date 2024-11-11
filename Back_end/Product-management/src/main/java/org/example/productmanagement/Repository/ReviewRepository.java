package org.example.productmanagement.Repository;

import org.example.productmanagement.Entites.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Finds a list of reviews associated with a specific product ID
    public List<Review> findByIdProducts(Long product);

    // Finds a review by product ID and user ID
    public Review findByIdProductsAndIduser(Long product, Long userid);
}
