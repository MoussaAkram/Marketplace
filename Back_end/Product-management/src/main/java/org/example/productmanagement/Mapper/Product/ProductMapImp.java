package org.example.productmanagement.Mapper.Product;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.productmanagement.DTO.Product.ProductReponseDTO;
import org.example.productmanagement.DTO.Product.ProductRequistDTO;
import org.example.productmanagement.Entites.Product;
import org.example.productmanagement.Repository.CategoryRepository;
import org.example.productmanagement.Web.UserControllers.UserOpenFeginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;


@Component// Indicates that this class is a Spring-managed component
public class ProductMapImp implements ProductMapInter {

    // Repository injections for accessing data
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserOpenFeginController userOpenFeginController;

    private final Cloudinary cloudinary;// Cloudinary instance for image uploads

    // Constructor to initialize Cloudinary with configuration values from application properties
    public ProductMapImp(@Value("${cloudinary.cloud-name}") String cloudName,
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
     * @param productRequistDTO the DTO containing category details
     * @return a Category entity populated with the provided details
     */
    @Override
    public Product FromRequist2Product(ProductRequistDTO productRequistDTO) {
        String imageUrl ;
        String publicId ;
        try {
            // Upload the image and get the URL and public ID
            Map uploadResult = uploadImage(productRequistDTO.getImage());
            imageUrl = (String) uploadResult.get("url");
            publicId = (String) uploadResult.get("public_id");
            // Create a new Product entity and set its properties
            Product product = new Product();
            product.setIdcategory(productRequistDTO.getIdcategory());
            product.setIdseller(productRequistDTO.getIdseller());
            product.setName(productRequistDTO.getName());
            product.setPrice(BigDecimal.valueOf(productRequistDTO.getPrice()));
            product.setStock(productRequistDTO.getStock());
            product.setDescription(productRequistDTO.getDescription());
            product.setDaTe_creation(productRequistDTO.getDaTe_creation());
            product.setImage(imageUrl);
            product.setImageKey(publicId);
            return product;
        } catch (IOException e) {
            throw new RuntimeException(e);// Handle the exception by rethrowing it as a runtime exc
        }

    }


    /**
     * Converts a Category entity to a CategoryReponseDTO object.
     * @param product the Category entity to convert
     * @return a CategoryReponseDTO object containing the category details
     */
    @Override
    public ProductReponseDTO FromProduct2Reponse(Product product) {
        ProductReponseDTO productReponseDTO = new ProductReponseDTO();
        // Set the properties of the DTO from the Product entity
        productReponseDTO.setIdcategory(product.getIdcategory());
        productReponseDTO.setIdseller(product.getIdseller());
        productReponseDTO.setIdProduct(product.getIdP());
        productReponseDTO.setName(product.getName());
        productReponseDTO.setDescription(product.getDescription());
        productReponseDTO.setDaTe_creation(product.getDaTe_creation());
        productReponseDTO.setPrice(product.getPrice());
        productReponseDTO.setStock(product.getStock());
        productReponseDTO.setImage(product.getImage());
        return productReponseDTO;
    }
}
