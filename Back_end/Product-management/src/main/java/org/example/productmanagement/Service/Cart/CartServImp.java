package org.example.productmanagement.Service.Cart;

import jakarta.transaction.Transactional;
import org.example.productmanagement.DTO.Cart.CartFrontDTO;
import org.example.productmanagement.DTO.Cart.CartReponseDTO;
import org.example.productmanagement.Entites.Cart;
import org.example.productmanagement.Entites.Product;
import org.example.productmanagement.Entites.Product_qte;
import org.example.productmanagement.Mapper.Cart.CartMapInter;
import org.example.productmanagement.Repository.CartRepository;
import org.example.productmanagement.Repository.ProductRepository;
import org.example.productmanagement.Repository.Product_qteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service // Indicates that this class is a service component in the Spring framework
@Transactional // Marks the class as transactional for database operations
public class CartServImp implements CartServInter {
	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartMapInter cartMapInter;

	@Autowired
	Product_qteRepository pqtRepository;

	// Adds a new cart and returns its ID
	@Override
	public Long ADD() {
		Cart cart = new Cart();
		List<Long> ids = new ArrayList<>();
		BigDecimal amount = BigDecimal.valueOf(0.00);
		cart.setAmont(amount);
		cart.setIdProductsqte(ids);
		cartRepository.save(cart);
		return cart.getIdC();
	}

	// Retrieves a single cart by its ID and converts it to a response DTO
	@Override
	public CartReponseDTO Getone(Long id) {
		return cartMapInter.FromCart2Reponse(cartRepository.findById(id).get());
	}

	// Retrieves a single cart by its ID and converts it to a front DTO
	@Override
	public CartFrontDTO GetoneCart(Long id) {
		return cartMapInter.FromCart2Front(cartRepository.findById(id).get());
	}

	// Retrieves all carts and converts them to response DTOs
	@Override
	public List<CartReponseDTO> Getall() {
		List<Cart> cartList = cartRepository.findAll();
		List<CartReponseDTO> cartReponseDTOList = new ArrayList<>();
		// Convert each Cart entity to a CartReponseDTO
		for (Cart cart : cartList) {
			cartReponseDTOList.add(cartMapInter.FromCart2Reponse(cart));
		}
		return cartReponseDTOList;
	}

	// Updates a product quantity in the cart
	@Override
	public void updateProductinCart(Long id,Product_qte product_qterequist) {
		Cart cart = cartRepository.findById(id).get();// Retrieve the cart by ID
		Product_qte product_qte1 = pqtRepository.findById(product_qterequist.getId()).get();
		Product product = productRepository.findById(product_qte1.getIdproduct()).get();

		BigDecimal price = product.getPrice();// Get the product price

		// Handle case where the new quantity is greater than the existing quantity
		if (product_qterequist.getQte() > product_qte1.getQte()) {
			int qte = product_qterequist.getQte() - product_qte1.getQte();// Calculate the difference in quantity
			BigDecimal quantity = BigDecimal.valueOf(qte); // Convert difference to BigDecimal

			cart.setAmont(cart.getAmont().add(price.multiply(quantity)));// Update cart amount

			product.setStock(product.getStock() - qte); // Decrease product stock
			productRepository.save(product);
		}
		// Handle case where the new quantity is less than the existing quantity
		if (product_qterequist.getQte() < product_qte1.getQte()) {
			int qte = product_qte1.getQte() - product_qterequist.getQte();// Calculate the difference in quantity
			BigDecimal quantity = BigDecimal.valueOf(qte); // Convert difference to BigDecimal

			cart.setAmont(cart.getAmont().subtract(price.multiply(quantity)));// Update cart amount

			product.setStock(product.getStock() + qte);// Increase product stock
			productRepository.save(product);// Save updated product stock
		}

		product_qte1.setQte(product_qterequist.getQte());// Save updated product stock
		pqtRepository.save(product_qte1); // Save the updated product quantity

		cartRepository.save(cart);
	}

	// Deletes a product from the cart
	@Override
	public void deleteProductformCart(Long id, Long idproductqte) {
		Cart cart = cartRepository.findById(id).get(); // Retrieve the cart by ID
		Product_qte productQte = pqtRepository.findById(idproductqte).get();// Get the product quantity
		Product product = productRepository.findById(productQte.getIdproduct()).get();// Get the product

		// Restore the product stock
		product.setStock(product.getStock() + productQte.getQte());
		productRepository.save(product);// Save updated product stock

		BigDecimal price = product.getPrice();// Get the product price
		BigDecimal quantity = BigDecimal.valueOf(productQte.getQte());// Convert quantity to BigDecimal
		BigDecimal amount = price.multiply(quantity);// Calculate total amount for the product

		cart.setAmont(cart.getAmont().subtract(amount));// Update cart amount to reflect removal

		List<Long> ids = new ArrayList<>();// Initialize a new list for product quantities

		// Build the new product quantity list excluding the deleted product
		for (Long i : cart.getIdProductsqte()) {
			if (!i.equals(idproductqte)) {
				ids.add(i);
			}
		}

		pqtRepository.deleteById(idproductqte);
		cart.setIdProductsqte(ids);
		cartRepository.save(cart);
	}


	// Adds a product to the cart
	@Override
	public void addProductToCart(Long id, Product_qte productQte1) {
		Cart cart = cartRepository.findById(id).get();// Retrieve the cart by ID
		Product product = productRepository.findById(productQte1.getIdproduct()).get(); // Get the product

		BigDecimal price = product.getPrice(); // Get the product price
		BigDecimal quantity = BigDecimal.valueOf(productQte1.getQte());// Convert quantity to BigDecimal
		BigDecimal amountToAdd = price.multiply(quantity); // Calculate the amount to add to the cart
		BigDecimal updatedAmount = cart.getAmont().add(amountToAdd); // Update the total cart amount

		List<Long> ids = cart.getIdProductsqte();// Get the current list of product quantities
		ids.add(productQte1.getId()); // Add the new product quantity ID to the list
		cart.setIdProductsqte(ids);// Update the cart's product quantity list
		cart.setAmont(updatedAmount);// Update the cart amount
		cartRepository.save(cart);// Save the updated cart
	}


	// Resets the cart, clearing product quantities and amount
	@Override
	public void reset(Long id) {
		Cart cart = cartRepository.findById(id).get(); // Retrieve the cart by ID
		cart.setIdProductsqte(null);// Clear the product quantity list
		cart.setAmont(BigDecimal.valueOf(0.00));// Reset the amount to 0

		cartRepository.save(cart);
	}
}
