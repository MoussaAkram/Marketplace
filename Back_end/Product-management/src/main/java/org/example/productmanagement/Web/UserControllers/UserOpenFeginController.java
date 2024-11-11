package org.example.productmanagement.Web.UserControllers;

import org.example.productmanagement.Model.Seller;
import org.example.productmanagement.Model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


// Declare this interface as a Feign client for communicating with the user service
@FeignClient(name = "USER-SERVICE")
public interface UserOpenFeginController {

    /**
     * Fetches a User by their ID from the USER-SERVICE.
     *
     * @param id the ID of the user to be fetched
     * @return the User object corresponding to the given ID
     */
    @GetMapping("/api/v1/admin/findOneUser/{id}")
    public User findOneUser(@PathVariable("id") Long id);


    /**
     * Fetches a Seller by their ID from the USER-SERVICE.
     *
     * @param sellerId the ID of the seller to be fetched
     * @return the Seller object corresponding to the given seller ID
     */
    @GetMapping("/api/v1/seller/{sellerId}")
    public Seller getSeller(@PathVariable("sellerId") Long sellerId);
}
