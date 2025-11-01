package com.pdds.controller;

import com.pdds.domain.User;
import com.pdds.domain.enums.Role;
import com.pdds.dto.*;
import com.pdds.security.TokenService;
import com.pdds.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User)auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@RequestBody @Valid UserDTO data){

        CreateUserDTO user = new CreateUserDTO(data.email(), data.password(), data.fullName(), data.birthday(), Role.USER);
        boolean registration = userService.create(user);

        if (!registration){
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Email already used"));
        }else{
            return ResponseEntity.status(201).body(new MessageResponseDTO("User created successfully"));
        }
    }

    @PostMapping("/register-adm")
    public ResponseEntity<MessageResponseDTO> registerAdm(@RequestBody @Valid UserDTO data){

        CreateUserDTO user = new CreateUserDTO(data.email(), data.password(), data.fullName(), data.birthday(), Role.ADMIN);
        boolean registration = userService.create(user);

        if (!registration){
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Email already used"));
        }else{
            return ResponseEntity.status(201).body(new MessageResponseDTO("User created successfully"));
        }
    }

}
