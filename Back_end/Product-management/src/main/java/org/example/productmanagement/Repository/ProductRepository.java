package org.example.productmanagement.Repository;


import org.example.productmanagement.Entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Finds a list of products associated with a specific seller ID
    List<Product> findByIdseller(Long ids);

    // Finds a list of products associated with a specific category ID
    List<Product> findByIdcategory(Long idcategory);
}
