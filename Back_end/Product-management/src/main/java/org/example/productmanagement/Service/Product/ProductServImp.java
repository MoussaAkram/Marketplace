package org.example.productmanagement.Service.Product;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import org.example.productmanagement.DTO.Product.ProductReponseDTO;
import org.example.productmanagement.DTO.Product.ProductRequistDTO;
import org.example.productmanagement.Entites.Product;
import org.example.productmanagement.Mapper.Product.ProductMapInter;
import org.example.productmanagement.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service // Indicates that this class is a service component in the Spring framework
@Transactional // Marks the class as transactional for database operations
public class ProductServImp implements ProductServInter {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductMapInter productMapInter;

	private final Cloudinary cloudinary; // Cloudinary instance for image upload and management

	// Constructor for initializing Cloudinary with configuration from application properties
	public ProductServImp(@Value("${cloudinary.cloud-name}") String cloudName,
						  @Value("${cloudinary.api-key}") String apiKey,
						  @Value("${cloudinary.api-secret}") String apiSecret) {
		cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret));
	}

	// Deletes an image from Cloudinary based on its public ID
	public Map deleteImage(String publicId) throws IOException {
		return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	}
	// Uploads an image to Cloudinary and returns the upload result
	public Map uploadImage(MultipartFile file) throws IOException {
		// Upload the file to Cloudinary
		Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		return uploadResult;
	}

	// Adds a new product
	@Override
	public void ADD(ProductRequistDTO productRequistDTO) {
		// Convert DTO to Product entity and save it
		productRepository.save(productMapInter.FromRequist2Product(productRequistDTO));
	}

	// Retrieves a single product by its ID
	@Override
	public ProductReponseDTO Getone(Long id) {
		// Convert the found product to a response DTO
		return productMapInter.FromProduct2Reponse(productRepository.findById(id).get());
	}

	// Retrieves all products for a specific seller
	@Override
	public List<ProductReponseDTO> GetProductForSeller(Long idseller) {
		List<Product> productList = productRepository.findByIdseller(idseller);
		List<ProductReponseDTO> productReponseDTOList = new ArrayList<>();
		// Convert each Product entity to a ProductReponseDTO
		for (Product product : productList) {
			productReponseDTOList.add(productMapInter.FromProduct2Reponse(product));
		}
		return productReponseDTOList;
	}

	// Retrieves all products for a specific category
	@Override
	public List<ProductReponseDTO> GetProductByCategory(Long idcategory) {
		List<Product> productList = productRepository.findByIdcategory(idcategory);
		List<ProductReponseDTO> productReponseDTOList = new ArrayList<>();
		// Convert each Product entity to a ProductReponseDTO
		for (Product product : productList) {
			productReponseDTOList.add(productMapInter.FromProduct2Reponse(product));
		}
		return productReponseDTOList;
	}

	// Retrieves all products
	@Override
	public List<ProductReponseDTO> Getall() {
		List<Product> products = productRepository.findAll();
		List<ProductReponseDTO> productReponseDTOs = new ArrayList<>();
		// Convert each Product entity to a ProductReponseDTO
		for (Product product : products) {
			productReponseDTOs.add(productMapInter.FromProduct2Reponse(product));
		}
		return productReponseDTOs;
	}

	// Updates an existing product
	@Override
	public void update(ProductRequistDTO productRequistDTO, Long id) {
		String imageUrl;
		String publicId;

		// Retrieve the product by its ID
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

		// Update the product fields only if they are provided in the DTO
		if (productRequistDTO.getPrice() != null) {
			product.setPrice(BigDecimal.valueOf(productRequistDTO.getPrice()));
		}
		if (productRequistDTO.getStock() != null) {
			product.setStock(productRequistDTO.getStock());
		}
		if (productRequistDTO.getName() != null) {
			product.setName(productRequistDTO.getName());
		}
		if (productRequistDTO.getIdcategory() != null) {
			product.setIdcategory(productRequistDTO.getIdcategory());
		}
		if (productRequistDTO.getDescription() != null) {
			product.setDescription(productRequistDTO.getDescription());
		}
		if (productRequistDTO.getDaTe_creation() != null) {
			product.setDaTe_creation(productRequistDTO.getDaTe_creation());
		}

		// Handle image update
		if (productRequistDTO.getImage() != null) {
			try {
				// Delete the old image using the image key
				deleteImage(product.getImageKey());

				// Upload the new image and get the URL and public ID
				Map<String, Object> uploadResult = uploadImage(productRequistDTO.getImage());
				imageUrl = (String) uploadResult.get("url");
				publicId = (String) uploadResult.get("public_id");

				// Set the new image URL and image key
				product.setImage(imageUrl);
				product.setImageKey(publicId);
			} catch (IOException e) {
				throw new RuntimeException("Failed to upload image", e);
			}
		}

		// Save the updated product back to the repository
		productRepository.save(product);
	}

	// Updates the stock of a product by a given quantity
	@Override
	public void updatestock(long id, int qte) {
		Product product = productRepository.findById(id).get();
		product.setStock(product.getStock() + qte);
		productRepository.save(product);
	}

	// Deletes a product by its ID
	@Override
	public void delete(Long id) {
		Product product = productRepository.findById(id).get();
        try {
			// Delete the product image from Cloudinary
            deleteImage(product.getImageKey());
			// Remove the product from the repository
			productRepository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}
