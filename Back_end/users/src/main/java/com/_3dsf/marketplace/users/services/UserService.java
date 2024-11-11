package com._3dsf.marketplace.users.services;


import com._3dsf.marketplace.users.bo.User;
import com._3dsf.marketplace.users.dao.UserRepository;
import com._3dsf.marketplace.users.dto.UserDto;
import com._3dsf.marketplace.users.exceptions.EmailAlreadyUsedException;
import com._3dsf.marketplace.users.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service

public class UserService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Retrieves a User by their ID. Throws an exception if the user is not found.
    public User getUserById(Long idUser) {

        return userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Checks if a user exists by their email address.
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Retrieves the ID of the user's cart.
    public Long getUserCart(Long idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getIdCart();
    }

    // Updates the user's information based on the provided UserDto.
    public void updateUser(Long idUser, UserDto userDto) {

        User user = getUserById(idUser);

        if (userDto.fullName() != null) {
            user.setFullName(userDto.fullName());
        }
        if (userDto.email() != null) {
            if (existsByEmail(userDto.email())) {
                throw new EmailAlreadyUsedException("This email already Used by another user");
            }
            user.setEmail(userDto.email());
        }
        if (userDto.country() != null) {
            user.setCountry(userDto.country());
        }
        if (userDto.address() != null) {
            user.setAddress(userDto.address());
        }
        if (userDto.phone() != null) {
            user.setPhone(userDto.phone());
        }
        if (userDto.password() != null) {
            user.setPassword(passwordEncoder.encode(userDto.password()));
        }
        userRepository.save(user);
    }
}
