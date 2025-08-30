package com.pdds.service;

import com.pdds.domain.Cart;
import com.pdds.domain.User;
import com.pdds.dto.ChangePasswordDTO;
import com.pdds.dto.UpdateUserDTO;
import com.pdds.dto.UserDTO;
import com.pdds.repository.UserRepository;
import com.pdds.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CartService cartService;

    @Autowired
    UserService(UserRepository userRepository, CartService cartService){
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public boolean update(Long id, UpdateUserDTO user){
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isEmpty()){
            return false;
        }

        User updatedUser = userToUpdate.get();

        updatedUser.setEmail(user.email());
        updatedUser.setFullName(user.fullName());
        updatedUser.setBirthday(Utils.stringToDate(user.birthday()));
        updatedUser.setRole(user.role());

        userRepository.save(updatedUser);

        return true;
    }

    public boolean delete(Long id){

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return false;

        Cart cart = cartService.findByUser(user.get()).get();

        cartService.delete(cart);
        userRepository.delete(user.get());
        return true;
    }

    public boolean changePassword(Long id, ChangePasswordDTO data){

        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) return false;

        User user = opt.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(data.oldPassword(), user.getPassword())) return false;

        user.setPassword(encoder.encode(data.newPassword()));
        userRepository.save(user);
        return true;
    }

    public UserDetails findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean create(UserDTO data){

        if (findByEmail(data.email()) != null){
            return false;
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User newUser = new User(data.email(), encryptedPassword, data.fullName(), Utils.stringToDate(data.birthday()), data.role());
        newUser = userRepository.save(newUser);

        // initiate shopping cart after creation of a user
        Cart cart = new Cart(new ArrayList<>(), newUser);
        cartService.create(cart);

        return true;
    }

}
