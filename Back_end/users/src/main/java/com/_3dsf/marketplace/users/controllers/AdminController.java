package com._3dsf.marketplace.users.controllers;

import com._3dsf.marketplace.users.bo.Admin;
import com._3dsf.marketplace.users.bo.auth.RegisterSellerRequest;
import com._3dsf.marketplace.users.dto.AdminDto;
import com._3dsf.marketplace.users.dto.CategoryDto;
import com._3dsf.marketplace.users.bo.Seller;
import com._3dsf.marketplace.users.bo.User;
import com._3dsf.marketplace.users.dto.UserDto;
import com._3dsf.marketplace.users.services.AdminService;
import com._3dsf.marketplace.users.services.SellerService;
import com._3dsf.marketplace.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
//@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class AdminController {

    private final UserService userService;
    private final SellerService sellerService;
    private final AdminService adminService;

    // Retrieves an admin by their ID
    @GetMapping("/{idAdmin}")
    public ResponseEntity<Admin> getAdmin(@PathVariable long idAdmin) {
        return ResponseEntity.ok(adminService.getAdminById(idAdmin));
    }

    // Adds a new admin
    @PostMapping("/")
    public ResponseEntity<?> addAdmin(@RequestBody Admin admin) {
        adminService.createAdmin(admin);
        return ResponseEntity.ok("Admin added successfully");
    }

    // Updates admin information by admin ID
    @PutMapping("/{idAdmin}")
    @Operation(summary = "update admin information by admins")
    public ResponseEntity<?> updateAdmin(@PathVariable("idAdmin") Long idAdmin,
                                         @RequestBody AdminDto adminDto) {
        adminService.updateAdmin(idAdmin, adminDto);
        return ResponseEntity.ok("Admin updated successfully");
    }

    // Updates user information by user ID
    @PutMapping("/updateUser/{idUser}")
    @Operation(summary = "update user information by admins")
    public ResponseEntity<?> updateUser(@PathVariable("idUser") Long idUser,
                                        @RequestBody UserDto userDto) {
        userService.updateUser(idUser, userDto);
        return ResponseEntity.ok("User updated successfully");
    }

    // Updates seller information by seller ID
    @PutMapping("/updateSeller/{idSeller}")
    @Operation(summary = "update seller information by admins")
    public ResponseEntity<?> updateSeller(@PathVariable("idSeller") Long idSeller,
                                          @RequestBody RegisterSellerRequest sellerDto) {
        sellerService.updateSeller(idSeller, sellerDto);
        return ResponseEntity.ok("Seller updated successfully");
    }

    // Removes a user by user ID
    @DeleteMapping("/removeUser/{idUser}")
    public ResponseEntity<?> removeUser(@PathVariable Long idUser) {
        adminService.removeUser(idUser);
        return ResponseEntity.ok("User removed successfully");
    }

    // Removes a seller by seller ID
    @DeleteMapping("/removeSeller/{idSeller}")
    public ResponseEntity<?> removeSeller(@PathVariable Long idSeller) {
        adminService.removeSeller(idSeller);
        return ResponseEntity.ok("Seller removed successfully");
    }

    // Removes an admin by admin ID
    @DeleteMapping("/removeAdmin/{idAdmin}")
    public ResponseEntity<?> removeAdmin(@PathVariable Long idAdmin) {
        adminService.removeAdmin(idAdmin);
        return ResponseEntity.ok("Admin removed successfully");
    }

    // Adds a new category
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        adminService.addCategory(categoryDto);
        return ResponseEntity.ok("Category added successfully");
    }

    // Updates a category by category ID
    @PutMapping("/updateCategory/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        adminService.updateCategory(categoryId, categoryDto);
        return ResponseEntity.ok("Category updated successfully");
    }

    // Removes a category by category ID
    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<?> removeCategory(@PathVariable("categoryId") Long categoryId) {
        adminService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category removed successfully");
    }

    // Finds a user by their ID
    @GetMapping("/findOneUser/{id}")
    public User findOneUser(@PathVariable("id") Long id) {
        return adminService.findOneUser(id);
    }

    // Lists all users
    @GetMapping("/listUsers")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(adminService.listUsersOnly());
    }

    // Lists all sellers
    @GetMapping("/listSellers")
    public ResponseEntity<List<Seller>> listSellers() {
        return ResponseEntity.ok(adminService.listSellers());
    }

    // Lists all admins
    @GetMapping("/listAdmins")
    public ResponseEntity<List<Admin>> listAdmins() {
        return ResponseEntity.ok(adminService.listAdmins());
    }
}