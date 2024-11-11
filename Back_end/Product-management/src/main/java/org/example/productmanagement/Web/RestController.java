package org.example.productmanagement.Web;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.productmanagement.DTO.Cart.CartFrontDTO;
import org.example.productmanagement.DTO.Cart.CartReponseDTO;
import org.example.productmanagement.DTO.Category.CategoryReponseDTO;
import org.example.productmanagement.DTO.Category.CategoryRequistDTO;
import org.example.productmanagement.DTO.Product.ProductReponseDTO;
import org.example.productmanagement.DTO.Product.ProductRequistDTO;
import org.example.productmanagement.DTO.Productqte.ProductqteReponsDTO;
import org.example.productmanagement.DTO.Productqte.ProductqteRequistDTO;
import org.example.productmanagement.DTO.Review.ReviewReponseDTO;
import org.example.productmanagement.DTO.Review.ReviewRequistDTO;
import org.example.productmanagement.Entites.Cart;
import org.example.productmanagement.Entites.Product;
import org.example.productmanagement.Entites.Product_qte;
import org.example.productmanagement.Entites.Review;
import org.example.productmanagement.InsufficientStockException;
import org.example.productmanagement.Mapper.Productqte.ProductqteMapInter;
import org.example.productmanagement.Model.CategoryData;
import org.example.productmanagement.Model.ProductData;
import org.example.productmanagement.Model.Rate;
import org.example.productmanagement.Repository.CartRepository;
import org.example.productmanagement.Repository.ProductRepository;
import org.example.productmanagement.Repository.Product_qteRepository;
import org.example.productmanagement.Repository.ReviewRepository;
import org.example.productmanagement.Service.Cart.CartServInter;
import org.example.productmanagement.Service.Category.CategoryServInter;
import org.example.productmanagement.Service.Product.ProductServInter;
import org.example.productmanagement.Service.Review.ReviewServInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
public class RestController {

    @Autowired
    CartServInter cartServInter;
    @Autowired
    CategoryServInter categoryServInter;
    @Autowired
    ProductServInter productServInter;
    @Autowired
    ReviewServInter reviewServInter;
    @Autowired
    ProductqteMapInter productqteMapInter;


    @Autowired
    ProductRepository pRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    Product_qteRepository product_qteRepository;
    @Autowired
    private CartRepository cartRepository;


    //                                   ###     Cart Controller    ###


    // Retrieve all carts
    @GetMapping("/carts")
    public List<CartReponseDTO> getCarts() {
       return cartServInter.Getall();
    }


    // Retrieve a specific cart by ID
    @GetMapping("/cartsV0/{id}")
    public CartReponseDTO getCartV0(@PathVariable Long id) {
        return cartServInter.Getone(id);
    }

    // Add a new cart
    @PostMapping("/carts")
    public Long addCart() {
         return cartServInter.ADD();
    }

    // Reset the specified cart
    @PutMapping("/cartsreset/{id}")
    public ResponseEntity<Void> resetCart(@PathVariable Long id) {
        try {
            cartServInter.reset(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


//                                   ###     Review Controller    ###

    // Retrieve a specific review by ID
    @GetMapping("/reviews/{id}")
    public ReviewReponseDTO getReview(@PathVariable Long id) {
            return reviewServInter.Getone(id);
    }



    //                                   ###    Product_qte Controller    ###
    // Get product quantity by product ID
    @GetMapping("/productsqteq/{id}")
    public ProductqteReponsDTO getProductsqteq(@PathVariable Long id) {
        return productqteMapInter.FromProduct2Reponse(id);

    }



    //    ######################################     FrontEnd    ##############################################


    //                                   ###     Cart Controller    ###

    // Update product quantity in cart
    @PutMapping("/cartsupdateqte/{id}")
    public void updateProductqteofCart(@PathVariable Long id, @RequestBody Product_qte productQte) {
    cartServInter.updateProductinCart(id, productQte);
    }

    // Create a new Product_qte object and set its fields
    @PutMapping("/cartsaddproduct/{id}")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long id, @RequestBody ProductqteRequistDTO productQte) {
        try {
            // Create a new Product_qte object and set its fields
            Product_qte productQte1 = new Product_qte();
            productQte1.setIdproduct(productQte.getIdproduct());
            productQte1.setQte(productQte.getQte());
            // Retrieve the product by ID
            Product product = pRepository.findById(productQte1.getIdproduct())
                    .orElseThrow(() -> new InsufficientStockException("Product not found"));
            // Check for sufficient stock
            if (product.getStock() < productQte1.getQte()) {
                throw new InsufficientStockException("Insufficient stock for product ID: " + productQte1.getIdproduct());
            }
            // Update product stock
            product.setStock(product.getStock() - productQte1.getQte());

            // Retrieve the cart
            Cart cart = cartRepository.findById(id)
                    .orElseThrow(() -> new Exception("Cart not found"));

            boolean productExistsInCart = false;
            // Check if the product already exists in the cart
            for (long i : cart.getIdProductsqte()) {
                Product_qte productQteInCart = product_qteRepository.findById(i)
                        .orElseThrow(() -> new Exception("Product_qte not found"));
                if (productQteInCart.getIdproduct().equals(productQte1.getIdproduct())) {
                    productQteInCart.setQte(productQteInCart.getQte() + productQte1.getQte());
                    productQte1 = productQteInCart;
                    productExistsInCart = true;
                    break;
                }
            }
            // Save updated product and product quantity
            pRepository.save(product);
            product_qteRepository.save(productQte1);

            // Update the cart total if the product was already in the cart
            if (!productExistsInCart) {
                cartServInter.addProductToCart(id, productQte1);// Add product to cart
            } else {
                BigDecimal currentAmount = cart.getAmont();
                BigDecimal productPrice = product.getPrice();
                BigDecimal totalPriceToAdd = productPrice.multiply(BigDecimal.valueOf(productQte1.getQte()));
                cart.setAmont(currentAmount.add(totalPriceToAdd)); // Update cart amount
                cartRepository.save(cart);
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (InsufficientStockException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Insufficient stock
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Cart or product not found
        }
    }

    @PutMapping("/cartsdeleteproduct/{id}")
    public ResponseEntity<Void> deleteProductFromCart(@PathVariable Long id, @RequestBody Long idproduct) {
        try {
            cartServInter.deleteProductformCart(id, idproduct);// Delete product from cart
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Cart or product not found
        }
    }

    // Retrieve a specific cart for the frontend
    @GetMapping("/carts/{id}")
    public CartFrontDTO getCart(@PathVariable("id") Long id) {
        return cartServInter.GetoneCart(id);
    }


    //                                   ###     Category Controller    ###

    // Retrieve all categories
    @GetMapping("/categorys")
    public List<CategoryReponseDTO> getCategories() {
        return categoryServInter.Getall();
    }

    // Retrieve a specific category by ID
    @GetMapping("/categorys/{id}")
    public CategoryReponseDTO getCategory(@PathVariable Long id) {
        return categoryServInter.Getone(id);
    }

    @PostMapping("/categorys")
    public ResponseEntity<Void> addCategory(
            @RequestPart("file") MultipartFile file,
            @RequestPart("categoryData") String categoryDataJson
    ) {
        // Convert the JSON string into a CategoryData object
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryData categoryData;

        try {
            categoryData = objectMapper.readValue(categoryDataJson, CategoryData.class);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Invalid JSON data
        }

        // Prepare the DTO
        CategoryRequistDTO categoryRequistDTO = new CategoryRequistDTO();
        categoryRequistDTO.setName(categoryData.getName());
        categoryRequistDTO.setPub(categoryData.getPub());
        categoryRequistDTO.setImage(file);

        try {
            categoryServInter.ADD(categoryRequistDTO);// Add the category
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Error adding category
        }
    }



    @PutMapping("/categorys/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id,  @RequestPart(value = "file" , required = false) MultipartFile file,
                                               @RequestPart("categoryData") String categoryDataJson
    ) {
        // Convert the JSON string into a CategoryData object
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryData categoryData;

        try {
            categoryData = objectMapper.readValue(categoryDataJson, CategoryData.class);// Invalid JSON data
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Prepare the DTO  for updating
        CategoryRequistDTO categoryRequistDTO = new CategoryRequistDTO();
        categoryRequistDTO.setName(categoryData.getName());
        categoryRequistDTO.setPub(categoryData.getPub());
        categoryRequistDTO.setImage(file);
        try {
            categoryServInter.update(categoryRequistDTO, id);// Update the category
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Error updating category
        }
    }

    @DeleteMapping("/categorys/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryServInter.delete(id);
            return new ResponseEntity<>(HttpStatus.OK); // Delete the category
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Category not found
        }
    }


    //                                   ###     Product Controller    ###

    // Retrieve all products
    @GetMapping("/products")
    public List<ProductReponseDTO> getProducts() {
        return productServInter.Getall();
    }

    // Retrieve a specific product by ID Category
    @GetMapping("/productsByCategory/{idcategory}")
    public List<ProductReponseDTO> getProductsByCategory(@PathVariable("idcategory") Long idcategory) {
        return productServInter.GetProductByCategory(idcategory);
    }

    @GetMapping("/getProductsBySeller/{id}")// Retrieve a specific product by ID Seller
    public List<ProductReponseDTO> getProductsBySeller(@PathVariable("id") Long id) {
        return productServInter.GetProductForSeller(id);
    }

    // Retrieve a specific product by ID
    @GetMapping("/products/{id}")
    public ProductReponseDTO getProduct(@PathVariable Long id) {
        return productServInter.Getone(id);

    }

    @PostMapping("/products")
    public ResponseEntity<Void> addProduct(
        @RequestPart("file") MultipartFile file,
        @RequestPart("productData") String productDataJson
    ) {
            // Convert the JSON string into a CategoryData object
            ObjectMapper objectMapper = new ObjectMapper();
            ProductData productData;

            try {
                productData = objectMapper.readValue(productDataJson, ProductData.class);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Invalid JSON data
            }

            // Prepare the DTO
            ProductRequistDTO productRequistDTO = new ProductRequistDTO();
            productRequistDTO.setName(productData.getName());
            productRequistDTO.setStock(productData.getStock());
            productRequistDTO.setDescription(productData.getDescription());
            productRequistDTO.setIdseller(productData.getIdseller());
            productRequistDTO.setIdcategory(productData.getIdcategory());
            productRequistDTO.setPrice(productData.getPrice());
            productRequistDTO.setDaTe_creation(productData.getDaTe_creation());
            productRequistDTO.setImage(file);

            try {
                productServInter.ADD(productRequistDTO);
                return new ResponseEntity<>(HttpStatus.CREATED); // Add the product
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Error adding product
            }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestPart(value = "file",required = false) MultipartFile file,
                                              @RequestPart("productData") String productDataJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductData productData;
        try {
            productData = objectMapper.readValue(productDataJson, ProductData.class);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Invalid JSON data
        }
        // Prepare the DTO for updating
        ProductRequistDTO productRequistDTO = new ProductRequistDTO();
        productRequistDTO.setName(productData.getName());
        productRequistDTO.setStock(productData.getStock());
        productRequistDTO.setDescription(productData.getDescription());
        productRequistDTO.setIdseller(productData.getIdseller());
        productRequistDTO.setIdcategory(productData.getIdcategory());
        productRequistDTO.setPrice(productData.getPrice());
        productRequistDTO.setDaTe_creation(productData.getDaTe_creation());
        productRequistDTO.setImage(file);
        try {
            productServInter.update(productRequistDTO, id); // Update the product
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Error updating product
        }
    }

    @PutMapping("/productstock/{id}/{qte}")
    public void updatestockProduct(@PathVariable("id") Long id ,@PathVariable("qte") int qte){
        productServInter.updatestock(id, qte);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productServInter.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);// Delete the product
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Product not found
        }
    }


    //                                   ###     Review Controller    ###


    @GetMapping("/reviewsforproduct/{idProduct}")
    public List<ReviewReponseDTO> getReviews(@PathVariable("idProduct") Long idProduct) {
        return reviewServInter.Getall(idProduct);
    }

   @GetMapping("/reviewsforUser/{product}/{userid}")
    public ReviewReponseDTO getReviewforUser(@PathVariable("product") Long product,@PathVariable("userid") Long userid) {
        return reviewServInter.Getforuser(product,userid);
    }

    @PostMapping("/reviews")
    public ResponseEntity<Void> addReview(@RequestBody ReviewRequistDTO reviewRequistDTO) {
        try {
            reviewServInter.ADD(reviewRequistDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);// Add a new review
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Error adding review
        }
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        try {
            reviewServInter.delete(id);
            return new ResponseEntity<>(HttpStatus.OK); // Delete the review
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Review not found
        }
    }


    //                                   ###     calcule rate    ###

    // Get average rating for a product
    @GetMapping("/rating/{id}")
    public Rate getRating(@PathVariable("id") Long id) {
        List<Review> reviewList = reviewRepository.findByIdProducts(id);
        double rating = 0;
        for (Review review : reviewList) {
            rating += review.getRating();
        }
        if (!reviewList.isEmpty()) {
            rating = rating / reviewList.size();
        }

        // Format rating to 2 decimal places
        rating = Math.round(rating * 100.0) / 100.0;

        Rate rate = new Rate();
        rate.setRating(rating);
        rate.setNbReview(reviewList.size());
        return rate;
    }


}




