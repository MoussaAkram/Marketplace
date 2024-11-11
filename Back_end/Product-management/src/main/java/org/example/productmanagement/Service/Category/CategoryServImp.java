package org.example.productmanagement.Service.Category;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import org.example.productmanagement.DTO.Category.CategoryReponseDTO;
import org.example.productmanagement.DTO.Category.CategoryRequistDTO;
import org.example.productmanagement.Entites.Category;
import org.example.productmanagement.Mapper.Category.CategoryMapInter;
import org.example.productmanagement.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service // Indicates that this class is a service component in the Spring framework
@Transactional // Marks the class as transactional for database operations
public class CategoryServImp implements CategoryServInter {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	CategoryMapInter categoryMapInter;


	private final Cloudinary cloudinary;// Cloudinary instance for image upload and management

	// Constructor for initializing Cloudinary with configuration from application properties
	public CategoryServImp(@Value("${cloudinary.cloud-name}") String cloudName,
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


	// Adds a new category to the repository
	@Override
	public void ADD(CategoryRequistDTO categoryRequistDTO) {
		categoryRepository.save(categoryMapInter.FromRequist2Category(categoryRequistDTO));
	}

	// Retrieves a single category by its ID
	@Override
	public CategoryReponseDTO Getone(Long id) {
		return categoryMapInter.FromCategorye2Reponse(categoryRepository.findById(id).get());
	}

	// Retrieves all categories
	@Override
	public List<CategoryReponseDTO> Getall() {
		List<Category> categoryList = categoryRepository.findAll();
		List<CategoryReponseDTO> categoryReponseDTOList = new ArrayList<>();
		// Convert each Category entity to a CategoryReponseDTO
		for (Category category : categoryList) {
			categoryReponseDTOList.add(categoryMapInter.FromCategorye2Reponse(category));
		}
		return categoryReponseDTOList;
	}

	// Updates an existing category
	@Override
	public void update(CategoryRequistDTO categoryRequistDTO, Long id) {
		String imageUrl ;
		String publicId ;
		// Retrieve the category by its ID
		Category category = categoryRepository.findById(id).get();
		// Update the category fields only if they are provided in the DTO
		if (categoryRequistDTO.getName() != null) {
			category.setName(categoryRequistDTO.getName());
		}
		if (categoryRequistDTO.getPub() != null){
			category.setPub(categoryRequistDTO.getPub());
		}
		// Handle image update
		if (categoryRequistDTO.getImage() != null){
			try {
				// Delete the old image using the image key
				deleteImage(category.getImageKey());

				// Upload the new image and get the URL and public ID
				Map uploadResult = uploadImage(categoryRequistDTO.getImage());
				imageUrl = (String) uploadResult.get("url");
				publicId = (String) uploadResult.get("public_id");

				// Set the new image URL and image key
				category.setImage(imageUrl);
				category.setImageKey(publicId);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		categoryRepository.save(category);
	}

	// Deletes a category by its ID
	@Override
	public void delete(Long id) {
		// Retrieve the category by its ID
		Category category = categoryRepository.findById(id).get();
		try {
			// Delete the category image from Cloudinary
			deleteImage(category.getImageKey());
			// Remove the category from the repository
			categoryRepository.deleteById(id);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
