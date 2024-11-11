package com.Marketplace.OrdreService.Web.userControllers;

import com.Marketplace.OrdreService.Model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * Feign client for communicating with the User Service.
 */
@FeignClient(name = "USER-SERVICE")

public interface UserOpenFeignController {

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User object associated with the specified ID
     */
    @GetMapping("/api/v1/admin/findOneUser/{id}")
    public User findOneUser(@PathVariable("id") Long id);


    /**
     * Retrieves the cart ID for a specific user.
     *
     * @param idUser the ID of the user whose cart to retrieve
     * @return the ID of the user's cart
     */
    @GetMapping("/api/v1/users/{idUser}/cart")
    public Long getUserCart(@PathVariable("idUser") Long idUser);
}
