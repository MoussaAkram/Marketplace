package com._3dsf.marketplace.users.controllers;

import com._3dsf.marketplace.users.bo.Seller;
import com._3dsf.marketplace.users.bo.auth.RegisterSellerRequest;
import com._3dsf.marketplace.users.clients.OrdreClient;
import com._3dsf.marketplace.users.clients.ProductClient;
import com._3dsf.marketplace.users.clients.WebPayClient;
import com._3dsf.marketplace.users.dto.AmountDto;
import com._3dsf.marketplace.users.dto.SellerDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com._3dsf.marketplace.users.dto.ProductDto;
import com._3dsf.marketplace.users.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
@Tag(name = "Seller")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    private final OrdreClient ordreClient;

    @Autowired
    private final WebPayClient webPayClient;

    // Retrieves a seller by their unique ID and returns the seller's details
    @GetMapping("/{idSeller}")
    public ResponseEntity<Seller> getSeller(@PathVariable("idSeller") Long idSeller) {
        return ResponseEntity.ok(sellerService.getSellerById(idSeller));
    }

    // Adds a new seller associated with a user ID, saves the seller, and updates client systems with the new seller's ID
    @PostMapping("/{idUser}")
    public ResponseEntity<?> addSeller(@PathVariable("idUser") Long idUser,
                                       @RequestBody SellerDto sellerDto)
    {
        Long idSeller = sellerService.addSeller(idUser, sellerDto);
        ordreClient.fixidUser(idUser, idSeller);
        webPayClient.fixidUser(idUser, idSeller);
        return ResponseEntity.ok("new seller added successfully with Id: " + idSeller);
    }

    // Updates the information of an existing seller based on their unique ID
    @PutMapping("/{idSeller}")
    public ResponseEntity<?> updateSeller(@PathVariable("idSeller") Long idSeller,
                                          @RequestBody RegisterSellerRequest sellerDto) {
        sellerService.updateSeller(idSeller, sellerDto);
        return ResponseEntity.ok("Seller updated successfully");
    }

    // Retrieves the total sold amount for a seller by their unique ID
    @GetMapping("/{idSeller}/sold")
    public AmountDto getSold(@PathVariable("idSeller") Long idSeller) {
        return new AmountDto(sellerService.getSellerSold(idSeller));
    }

    // Increases the sold amount for a seller based on the provided ID and amount
    @PutMapping("/{idSeller}/increaseSold")
    public void increaseSold(@PathVariable("idSeller") Long idSeller,
                             @RequestBody Double amountDto)
    {
        sellerService.increaseSellerSold(idSeller, amountDto);
    }

    // Decreases the sold amount for a seller based on the provided ID and amount
    @PutMapping("/{idSeller}/decreaseSold")
    public void decreaseSold(@PathVariable("idSeller") Long idSeller,
                             @RequestBody Double amountDto)
    {
        sellerService.decreaseSellerSold(idSeller, amountDto);
    }

    // Lists all products associated with a seller by their unique ID
    @GetMapping("/{idSeller}/products")
    public ResponseEntity<List<ProductDto>> listSellerProducts(@PathVariable("idSeller") Long idSeller) {
        return ResponseEntity.ok(sellerService.getProductsBySellerId(idSeller));
    }

    // Adds a new product associated with a seller
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto product) {
        sellerService.addProduct(product);
        return ResponseEntity.ok("Product added successfully");
    }

    // Updates an existing product based on its unique ID
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDto product) {
        sellerService.updateProduct(productId, product);
        return ResponseEntity.ok("Product updated successfully");
    }

    // Removes a product based on its unique ID
    @DeleteMapping("/removeProduct/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable("productId") Long productId) {
        sellerService.removeProduct(productId);
        return ResponseEntity.ok("Product removed successfully");
    }
}
