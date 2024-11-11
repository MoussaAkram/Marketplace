package org.example.productmanagement.Mapper.Cart;

import org.example.productmanagement.DTO.Cart.CartFrontDTO;
import org.example.productmanagement.DTO.Cart.CartReponseDTO;
import org.example.productmanagement.Entites.Cart;
import org.example.productmanagement.Entites.Product;
import org.example.productmanagement.Entites.Product_qte;
import org.example.productmanagement.Repository.ProductRepository;
import org.example.productmanagement.Repository.Product_qteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component// Indicates that this class is a Spring-managed component
public class CartMapImp implements CartMapInter {

	// Repository injections for accessing data

	@Autowired
	Product_qteRepository product_qteRepository;
	@Autowired
	ProductRepository productRepository;


	/**
	 * Converts a Cart entity to a CartReponseDTO object.
	 * @param cart the Cart entity to convert
	 * @return a CartReponseDTO object containing cart details
	 */
	@Override
	public CartReponseDTO FromCart2Reponse(Cart cart) {
		CartReponseDTO cartReponseDTO = new CartReponseDTO();
		cartReponseDTO.setId(cart.getIdC());// Set the ID of the cart
		List<Product_qte> productQteList = new ArrayList<>();// List to hold Product_qte entities
		for (Long p : cart.getIdProductsqte()) { // Iterate over product quantity IDs in the cart
			// Fetch the Product_qte entity by its ID and add it to the list
			Product_qte productQte = product_qteRepository.findById(p).get();
			productQteList.add(productQte);
		}
		cartReponseDTO.setProductQteList(productQteList);// Set the product quantity list in the DTO
		cartReponseDTO.setAmont(cart.getAmont()); // Set the total amount in the DTO
		return cartReponseDTO;
	}


	/**
	 * Converts a Cart entity to a CartFrontDTO object.
	 * This DTO is intended for front-end use, including product details.
	 * @param cart the Cart entity to convert
	 * @return a CartFrontDTO object containing cart and product details
	 */
	@Override
	public CartFrontDTO FromCart2Front(Cart cart) {
		CartFrontDTO cartFrontDTO = new CartFrontDTO();
		cartFrontDTO.setId(cart.getIdC()); // Set the ID of the cart
		List<Product_qte> productQteList = new ArrayList<>(); // List to hold Product_qte entities
		for (Long p : cart.getIdProductsqte()) {// Iterate over product quantity IDs in the cart
			// Fetch the Product_qte entity by its ID
			Product_qte productQte = product_qteRepository.findById(p).get();
			// Set the associated Product entity in the Product_qte
			productQte.setProduct(productRepository.findById(productQte.getIdproduct()).get());
			productQteList.add(productQte);// Add Product_qte to the list
		}
		cartFrontDTO.setProductQteList(productQteList);
		cartFrontDTO.setAmont(cart.getAmont());
		return cartFrontDTO;
	}
}
