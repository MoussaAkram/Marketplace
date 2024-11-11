package com.Marketplace.OrdreService.Service;

import com.Marketplace.OrdreService.DTO.OrderRequestDTO;
import com.Marketplace.OrdreService.DTO.OrderResponseDTO;
import com.Marketplace.OrdreService.Entities.Ordre;
import com.Marketplace.OrdreService.Entities.Status;
import com.Marketplace.OrdreService.Mapper.OrdreMapInter;
import com.Marketplace.OrdreService.Model.Product;
import com.Marketplace.OrdreService.Model.Productqte;
import com.Marketplace.OrdreService.Repository.OrdreRepository;
import com.Marketplace.OrdreService.Web.productControllers.ProductOpenFeignController;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the OrdreService interface for handling order-related operations.
 */
@Service
@Transactional
public class OrdreServiceImpl implements OrdreService{

    @Autowired
    private OrdreRepository ordreRepo;

    @Autowired
    private OrdreMapInter ordreMapInter;

    @Autowired
    private ProductOpenFeignController productOpenFeignController;

    /**
     * Creates a new order for the specified user.
     *
     * @param userid the ID of the user placing the order
     * @return the ID of the created order
     */
    @Override
    public Long ADD(Long userid) {
        Ordre ordre = ordreMapInter.FromRequist2Ordre(userid);
        ordreRepo.save(ordre);
        return ordre.getId();

    }



    /**
     * Retrieves a specific order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the OrderResponseDTO object containing order details
     */
    @Override
    public OrderResponseDTO GetoneOrdre(Long id) {
        OrderResponseDTO responseDTO = ordreMapInter.FromOrdre2Reponse(ordreRepo.findById(id).get());
        return responseDTO;
    }


    /**
     * Updates the status of an order.
     * If the status is FAILED, the stock for the associated products is updated.
     *
     * @param ide the ID of the order to update
     * @param status the new status of the order
     */
    @Override
    public void Update(Long ide, Status status) {


        System.out.println(status);
        Ordre ordre = ordreRepo.findById(ide).orElseThrow(() -> new RuntimeException("Ordre not found"));

        if (status == Status.SUCCESS) {
            ordre.setStatus(status);
            ordreRepo.save(ordre);
        }

        if (status == Status.FAILED) {
            for (Long productId : ordre.getProductIds()) {
                Productqte productqte = productOpenFeignController.getProductsqteq(productId);
                Long id = productqte.getProduct().getIdProduct();
                int qte = productqte.getQte();
                productOpenFeignController.updatestockProduct(id,qte);
            }
            ordre.setStatus(status);
            ordreRepo.save(ordre);
        }
    }


    /**
     * Retrieves all orders for a specified user.
     *
     * @param userid the ID of the user whose orders are to be retrieved
     * @return a list of OrderResponseDTO objects containing order details
     */
    @Override
    public List<OrderResponseDTO> GetallOrdre(Long userid) {
        List<Ordre> ordreList = ordreRepo.findByIdUser(userid);
        List<OrderResponseDTO> responseDTOList = new ArrayList<>();
        for (Ordre ordre : ordreList) {
            responseDTOList.add(ordreMapInter.FromOrdre2Reponse(ordre));
        }
        return responseDTOList;
    }


    /**
     * Retrieves all orders for admin purposes.
     *
     * @return a list of OrderResponseDTO objects containing all orders
     */
    @Override
    public List<OrderResponseDTO> GetallOrdreadmin() {
        List<Ordre> ordreList = ordreRepo.findAll();
        List<OrderResponseDTO> responseDTOList = new ArrayList<>();
        for (Ordre ordre : ordreList) {
            responseDTOList.add(ordreMapInter.FromOrdre2Reponse(ordre));
        }
        return responseDTOList;
    }


    /**
     * Deletes an order by its ID.
     *
     * @param id the ID of the order to delete
     */
    @Override
    public void delete(Long id) {
        ordreRepo.deleteById(id);
    }


    /**
     * Updates the user associated with orders.
     *
     * @param idUser the current user ID
     * @param newidUser the new user ID to associate with the orders
     */
    @Override
    public void updateUserordre(Long idUser, Long newidUser) {
        List<Ordre> ordreList = ordreRepo.findByIdUser(idUser);
        for (Ordre ordre : ordreList) {
           ordre.setIdUser(newidUser);
           ordreRepo.save(ordre);
        }
    }
}
