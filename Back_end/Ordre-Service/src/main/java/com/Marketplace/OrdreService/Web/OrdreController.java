package com.Marketplace.OrdreService.Web;

import com.Marketplace.OrdreService.DTO.OrderRequestDTO;
import com.Marketplace.OrdreService.DTO.OrderResponseDTO;
import com.Marketplace.OrdreService.Entities.Status;
import com.Marketplace.OrdreService.Model.User;
import com.Marketplace.OrdreService.Service.OrdreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")// Base URL for this controller
public class OrdreController {

    @Autowired
    private OrdreService ordreServInter;// Service for order management
    private Long id;


    /**
     * Creates a new order for the given user ID.
     *
     * @param userid the ID of the user placing the order
     * @return the ID of the newly created order
     */
    @PostMapping("/createOrdre/{userid}")
    public Long addOrdre(@PathVariable("userid") Long userid) {
        return ordreServInter.ADD(userid);
    }


    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the order response DTO if found, or a 404 Not Found response
     */
    @GetMapping("/oneordres/{id}")
    public ResponseEntity<OrderResponseDTO> getOrdreById(@PathVariable("id") Long id) {
        OrderResponseDTO ordre = ordreServInter.GetoneOrdre(id);
        return ordre != null ? ResponseEntity.ok(ordre) : ResponseEntity.notFound().build();
    }



    /**
     * Updates the status of an order by its ID.
     *
     * @param id the ID of the order to update
     * @param status the new status to set for the order
     * @return a 201 Created response
     */
    @PutMapping("/ordres/{id}")
    public ResponseEntity<Void> fixStatusOrdre(@PathVariable("id") Long id,@RequestBody  Status status) {
        ordreServInter.Update(id,status);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * Retrieves all orders for a specific user.
     *
     * @param userid the ID of the user whose orders to retrieve
     * @return a list of order response DTOs for the user
     */
    @GetMapping("/ordres/{userid}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdres(@PathVariable("userid") Long userid ) {
        return ResponseEntity.ok(ordreServInter.GetallOrdre(userid));
    }



    /**
     * Retrieves all orders for admin purposes.
     *
     * @return a list of all order response DTOs
     */
    @GetMapping("/ordresadmin")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdresadmin() {
        return ResponseEntity.ok(ordreServInter.GetallOrdreadmin());
    }


    /**
     * Deletes an order by its ID.
     *
     * @param id the ID of the order to delete
     * @return a 204 No Content response
     */
    @DeleteMapping("/ordres/{id}")
    public ResponseEntity<Void> deleteOrdre(@PathVariable Long id) {
        ordreServInter.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves the user associated with an order by its ID.
     *
     * @param id the ID of the order
     * @return the User associated with the order
     */
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") Long id) {

        OrderResponseDTO ordre = ordreServInter.GetoneOrdre(id);

        // Vérifie si l'objet `ordre` n'est pas nul et retourne l'ID de l'utilisateur associé
        return ordre.getUser();
    }


    /**
     * Updates the user ID associated with an order.
     *
     * @param idUser the current user ID
     * @param newIduser the new user ID to set
     */
    @PutMapping("/ordres/fixidUser/{idUser}/{newIduser}")
    public void fixidUser(@PathVariable("idUser") Long idUser, @PathVariable("newIduser") Long newIduser) {
        ordreServInter.updateUserordre(idUser,newIduser);
        System.out.println("idUser = "+idUser+" newId user = " + newIduser);
    }

}
