package com.Marketplace.OrdreService.Mapper;

import com.Marketplace.OrdreService.DTO.OrderRequestDTO;
import com.Marketplace.OrdreService.DTO.OrderResponseDTO;
import com.Marketplace.OrdreService.Entities.Ordre;
import com.Marketplace.OrdreService.Entities.Status;
import com.Marketplace.OrdreService.Model.Cart;
import com.Marketplace.OrdreService.Model.Productqte;
import com.Marketplace.OrdreService.Model.User;
import com.Marketplace.OrdreService.Web.productControllers.ProductOpenFeignController;
import com.Marketplace.OrdreService.Web.userControllers.UserOpenFeignController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrdreMapImp implements OrdreMapInter{
    @Autowired
    private ProductOpenFeignController productOpenFeignController;

    @Autowired
    private UserOpenFeignController userOpenFeignController;


    @Override
    public Ordre FromRequist2Ordre(Long userid) {
        Ordre ordre = new Ordre();
        // Get user's cart ID
        ordre.setIdUser(userid);
        Long idcart = userOpenFeignController.getUserCart(ordre.getIdUser());
        Cart cart = productOpenFeignController.getCartV0(idcart);

        // Extract product IDs from the cart
        List<Long> ids = new ArrayList<>();
        for (Productqte productqte: cart.getProductQteList()) {
            ids.add(productqte.getId());
        }
        ordre.setProductIds(ids);
        ordre.setAmount(cart.getAmont());

        // Set the order date
        LocalDateTime ldt = LocalDateTime.now();
        // Define the date format as dd/MM/yy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        // Format the current date
        String formattedDate = ldt.format(formatter);
        ordre.setOrdreDate(formattedDate);

        // Set initial status
        ordre.setStatus(Status.LISTEN);

        // Reset the cart after creating the order
        productOpenFeignController.resetCart(cart.getId());
        return ordre;
    }

    @Override
    public OrderResponseDTO FromOrdre2Reponse(Ordre ordre) {
        OrderResponseDTO ordreReponseDTO = new OrderResponseDTO();
        // Map order properties to response DTO
        ordreReponseDTO.setIdOrdre(ordre.getId());
        User user = userOpenFeignController.findOneUser(ordre.getIdUser());
        ordreReponseDTO.setUser(user);
        ordreReponseDTO.setDate(ordre.getOrdreDate());
        ordreReponseDTO.setAmount(ordre.getAmount());
        ordreReponseDTO.setStatus(ordre.getStatus());

        // Retrieve product quantities associated with the order
        List<Productqte> products = new ArrayList<>();
        for (long id: ordre.getProductIds()) {
            Productqte productqte = productOpenFeignController.getProductsqteq(id);
            products.add(productqte);
        }
        ordreReponseDTO.setProductqtes(products);
        return ordreReponseDTO;
    }
}
