package com.pdds.controller;

import com.pdds.domain.User;
import com.pdds.domain.enums.Role;
import com.pdds.dto.ChangePasswordDTO;
import com.pdds.dto.MessageResponseDTO;
import com.pdds.dto.UpdateUserDTO;
import com.pdds.dto.UserDTO;
import com.pdds.service.UserService;
import com.pdds.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){

        List<User> users = userService.getAll();
        return ResponseEntity.ok().body(users);

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable @Valid Long id){
        Optional<User> opt = userService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(opt.get());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<MessageResponseDTO> updateUser(@PathVariable @Valid Long id, @RequestBody @Valid UpdateUserDTO data){

        boolean update = userService.update(id, data);

        if (!update){
            return ResponseEntity.badRequest().body(new MessageResponseDTO("There was a problem updating the user"));
        }else{
            return ResponseEntity.ok().body(new MessageResponseDTO("User updated successfully"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable @Valid Long id){

        boolean delete = userService.delete(id);

        if (!delete) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.status(204).build();
        }

    }

    @PostMapping("/{id}/change-pw")
    public ResponseEntity<MessageResponseDTO> changePassword(@PathVariable @Valid Long id, @RequestBody @Valid ChangePasswordDTO data, @AuthenticationPrincipal User principal){

        if (principal.getRole() != Role.ADMIN && !id.equals(principal.getId())) return ResponseEntity.badRequest().build();

        boolean changePassword = userService.changePassword(id, data);

        if (!changePassword) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.status(200).body(new MessageResponseDTO("Password updated"));
        }

    }

}
