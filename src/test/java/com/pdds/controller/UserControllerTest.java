package com.pdds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdds.config.TestConfig;
import com.pdds.domain.User;
import com.pdds.domain.enums.Role;
import com.pdds.dto.ChangePasswordDTO;
import com.pdds.dto.UserDTO;
import com.pdds.repository.UserRepository;
import com.pdds.security.TokenService;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TestConfig testConfig;

    private String tokenAdm;
    private String tokenUser;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build()
        ;
        objectMapper = new ObjectMapper();

        testConfig.cleanDatabase();

        tokenAdm = tokenService.generateToken((User) userRepository.findByEmail("admin@example.com"));
        tokenUser = tokenService.generateToken((User) userRepository.findByEmail("user@example.com"));

    }

    @Test
    public void getAllUsersTest() throws Exception{

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.not(Matchers.empty())))
        ;

    }

    @Test
    public void failGetAllUsersIfUserIsNotAdminTest() throws Exception{

        try {
            mockMvc.perform(get("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenUser))
                    .andExpect(status().isForbidden())
            ;
        } catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }

    }

    @Test
    public void getUserTest() throws Exception{

        User user = (User) userRepository.findByEmail("justin.lucas@example.com");

        mockMvc.perform(get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName").value("Justin Lucas"))
        ;
    }

    @Test
    public void failGetIfUserIsNotAdminTest() throws Exception{

        User user = (User) userRepository.findByEmail("justin.lucas@example.com");

        try{
            mockMvc.perform(get("/users/" + user.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenUser))
                    .andExpect(status().isForbidden())
            ;
        } catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }
    }

    @Test
    public void updateUserTest() throws Exception{

        User user = (User)userRepository.findByEmail("fabian.mills@example.com");

        UserDTO updatedUser = new UserDTO(
                "john.wick@email.com",
                "ABC123",
                "Jonathan Wick",
                "01/11/1985",
                Role.USER
        );

        String jsonRequest = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(post("/users/update/"+ user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
        ;
    }

    @Test
    public void failUpdateIfUserIsNotAdminTest() throws Exception{

        User user = (User)userRepository.findByEmail("fabian.mills@example.com");
        UserDTO updatedUser = new UserDTO(
                "john.wick@email.com",
                "ABC123",
                "Jonathan Wick",
                "01/11/1985",
                Role.USER
        );

        String jsonRequest = objectMapper.writeValueAsString(updatedUser);

        try{
            mockMvc.perform(post("/users/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenUser)
                            .content(jsonRequest))
                    .andExpect(status().isForbidden())
            ;
        } catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }

    }

    @Test
    public void deleteUserByIdTest() throws Exception{

        User user = (User)userRepository.findByEmail("whitney.clark@example.com");

        mockMvc.perform(delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm))
                .andExpect(status().isNoContent())
        ;

    }

    @Test
    public void failDeleteIfUserIsNotAdminTest() throws Exception{

        User user = (User)userRepository.findByEmail("whitney.clark@example.com");

        try{
            mockMvc.perform(delete("/users/" + user.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenUser))
                    .andExpect(status().isForbidden())
            ;
        } catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }

    }

    @Test
    public void changePassword() throws Exception {

        User user = (User) userRepository.findByEmail("justin.lucas@example.com");
        ChangePasswordDTO cp = new ChangePasswordDTO("password", "newpassword");

        String jsonRequest = objectMapper.writeValueAsString(cp);

        mockMvc.perform(post("/users/"+ user.getId() + "/change-pw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated"))
        ;

    }

}
