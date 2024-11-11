package org.example.productmanagement.Mapper.Category;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.productmanagement.DTO.Category.CategoryReponseDTO;
import org.example.productmanagement.DTO.Category.CategoryRequistDTO;
import org.example.productmanagement.Entites.Category;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Component// Indicates that this class is a Spring-managed component
public class CategoryMapImp implements CategoryMapInter {

	private final Cloudinary cloudinary;// Cloudinary instance for image uploads

	// Constructor to initialize Cloudinary with configuration values from application properties
	public CategoryMapImp(@Value("${cloudinary.cloud-name}") String cloudName,
						  @Value("${cloudinary.api-key}") String apiKey,
						  @Value("${cloudinary.api-secret}") String apiSecret) {
		cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret));
	}

	/**
	 * Uploads an image file to Cloudinary.
	 * @param file the image file to upload
	 * @return a map containing the upload result, including the image URL and public ID
	 * @throws IOException if an error occurs during file upload
	 */
	public Map uploadImage(MultipartFile file) throws IOException {
		// Upload the file to Cloudinary
		Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		return uploadResult;
	}



	/**
	 * Converts a CategoryRequistDTO object to a Category entity.
	 * @param categoryRequistDTO the DTO containing category details
	 * @return a Category entity populated with the provided details
	 */
	@Override
	public Category FromRequist2Category(CategoryRequistDTO categoryRequistDTO) {
		String imageUrl ;
		String publicId ;
		try {
			// Upload the image and get the URL and public ID
			Map uploadResult = uploadImage(categoryRequistDTO.getImage());
			 imageUrl = (String) uploadResult.get("url");
			 publicId = (String) uploadResult.get("public_id");
			// Create a new Category entity and set its properties
			Category category = new Category();
			category.setName(categoryRequistDTO.getName());
			category.setPub(categoryRequistDTO.getPub());
			category.setImage(imageUrl);
			category.setImageKey(publicId);
			return category;
		} catch (IOException e) {
			throw new RuntimeException(e); // Handle the exception by rethrowing it as a runtime exc
		}
	}

	/**
	 * Converts a Category entity to a CategoryReponseDTO object.
	 * @param category the Category entity to convert
	 * @return a CategoryReponseDTO object containing the category details
	 */
	@Override
	public CategoryReponseDTO FromCategorye2Reponse(Category category) {
		CategoryReponseDTO categoryReponseDTO = new CategoryReponseDTO();
		// Set the properties of the DTO from the Category entity
		categoryReponseDTO.setId(category.getId());
		categoryReponseDTO.setName(category.getName());
		categoryReponseDTO.setImage(category.getImage());
		categoryReponseDTO.setPub(category.getPub());
		return categoryReponseDTO;
	}
}
