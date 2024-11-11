package org.example.productmanagement.Repository;

import org.example.productmanagement.Entites.Product_qte;
import org.example.productmanagement.Entites.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface Product_qteRepository extends JpaRepository<Product_qte, Long> {

    // Finds a Product_qte entity by its associated product ID
    Product_qte findByIdproduct(Long idproduct);

    // Checks if a Product_qte entity exists for a given product ID
    Boolean existsProduct_qteByIdproduct(Long idproduct );
}
