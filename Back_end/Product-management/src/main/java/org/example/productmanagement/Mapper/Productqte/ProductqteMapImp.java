package org.example.productmanagement.Mapper.Productqte;

import org.example.productmanagement.DTO.Productqte.ProductqteReponsDTO;
import org.example.productmanagement.Entites.Product;
import org.example.productmanagement.Entites.Product_qte;
import org.example.productmanagement.Mapper.Product.ProductMapInter;
import org.example.productmanagement.Repository.CategoryRepository;
import org.example.productmanagement.Repository.ProductRepository;
import org.example.productmanagement.Repository.Product_qteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component// Indicates that this class is a Spring-managed component
public class ProductqteMapImp implements ProductqteMapInter {

    // Repository injections for accessing data
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    Product_qteRepository product_qteRepository;
    @Autowired
    ProductMapInter productMapInter;// Interface for mapping product details

    /**
     * Converts a Product_qte entity identified by its ID to a ProductqteReponsDTO.
     *
     * @param id the ID of the Product_qte entity to be converted
     * @return a ProductqteReponsDTO populated with product and quantity details
     */
    @Override
    public ProductqteReponsDTO FromProduct2Reponse(Long id) {
        // Retrieve the Product_qte entity from the repository using the provided ID
        Product_qte productQte = product_qteRepository.findById(id).get();
        ProductqteReponsDTO productqteReponsDTO = new ProductqteReponsDTO();
        // Set the product ID in the response DTO
        productqteReponsDTO.setId(productQte.getIdproduct());
        // Retrieve the corresponding Product entity
        Product product = productRepository.findById(productQte.getIdproduct()).get();
        // Map the Product entity to a DTO using the ProductMapInter
        productqteReponsDTO.setProduct(productMapInter.FromProduct2Reponse(product));
        // Set the quantity in the response DTO
        productqteReponsDTO.setQte(productQte.getQte());
        // Print the mapped product details for debugging purposes
        System.out.println(productqteReponsDTO.getProduct());
        return productqteReponsDTO;
    }
}
