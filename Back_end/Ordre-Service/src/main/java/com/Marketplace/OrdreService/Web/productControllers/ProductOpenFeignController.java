package com.Marketplace.OrdreService.Web.productControllers;

import com.Marketplace.OrdreService.Model.Cart;
import com.Marketplace.OrdreService.Model.Product;
import com.Marketplace.OrdreService.Model.Productqte;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * Feign client for communicating with the Product Management Service.
 */
@FeignClient(name = "Product-management")

public interface ProductOpenFeignController {

    /**
     * Retrieves all products from the product management service.
     *
     * @return a list of Product objects
     */
    @GetMapping("/api/products")
    public List<Product> find_allProducts();

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the Product object associated with the specified ID
     */
    @GetMapping("/api/products/{id}")
    Product findProductsByid(@PathVariable("id") Long id);

    /**
     * Retrieves the quantity information for a specific product.
     *
     * @param id the ID of the product to retrieve quantity information for
     * @return the Productqte object containing quantity details for the specified product
     */
    @GetMapping("/api/productsqteq/{id}")
    public Productqte getProductsqteq(@PathVariable Long id);

    /**
     * Resets the cart for a specific user or cart ID.
     *
     * @param id the ID of the cart to reset
     */
    @PutMapping("/api/cartsreset/{id}")
    public void resetCart(@PathVariable Long id);

    /**
     * Retrieves the cart details for a specific user or cart ID.
     *
     * @param id the ID of the cart to retrieve
     * @return the Cart object associated with the specified ID
     */
    @GetMapping("/api/cartsV0/{id}")
    public Cart getCartV0(@PathVariable Long id);


    /**
     * Updates the stock for a specific product.
     *
     * @param id the ID of the product to update
     * @param qte the quantity to set for the product
     */
    @PutMapping("/api/productstock/{id}/{qte}")
    public void updatestockProduct(@PathVariable("id") Long id , @PathVariable("qte") int qte);
}
