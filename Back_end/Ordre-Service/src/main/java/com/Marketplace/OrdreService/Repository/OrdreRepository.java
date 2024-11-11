package com.Marketplace.OrdreService.Repository;

import com.Marketplace.OrdreService.Entities.Ordre;
import com.Marketplace.OrdreService.Entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Ordre entities.
 * Provides methods to perform CRUD operations and custom queries.
 */
public interface OrdreRepository extends JpaRepository<Ordre, Long> {

    /**
     * Finds all orders associated with a specific user ID.
     *
     * @param idUser the ID of the user whose orders are to be retrieved
     * @return a list of Ordre entities associated with the specified user ID
     */
    List<Ordre> findByIdUser(long idUser);
}
