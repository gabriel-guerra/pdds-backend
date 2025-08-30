package com.pdds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdds.config.TestConfig;
import com.pdds.domain.User;
import com.pdds.domain.enums.Role;
import com.pdds.dto.AuthenticationDTO;
import com.pdds.dto.UserDTO;
import com.pdds.repository.UserRepository;
import com.pdds.security.TokenService;
import com.pdds.utils.Utils;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private TestConfig testConfig;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build()
        ;
        objectMapper = new ObjectMapper();

        testConfig.cleanDatabase();

    }

    @Test
    public void registerUserTest() throws Exception{

        UserDTO userDto = new UserDTO(
                "mary.jane@example.com",
                "securepassword123",
                "Mary Jane",
                "01/01/1980",
                Role.USER
        );

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User created successfully"))
        ;

        User user = (User) userRepository.findByEmail(userDto.email());

        Assertions.assertEquals(userDto.email(), user.getEmail());
        Assertions.assertEquals(userDto.fullName(), user.getFullName());
        Assertions.assertEquals(userDto.role(), user.getRole());

    }

    @Test
    public void failRegisterAdminOnOpenEndpointTest() throws Exception{

        UserDTO userDto = new UserDTO(
                "john.doe@example.com",
                "securepassword123",
                "John Doe",
                "01/01/1980",
                Role.ADMIN
        );

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    public void registerAdminTest() throws Exception{

        User adm = (User) userRepository.findByEmail("john.doe@example.com");
        String token = tokenService.generateToken(adm);

        UserDTO userDto = new UserDTO(
                "mary.jane@example.com",
                "securepassword123",
                "Mary Jane",
                "01/01/1980",
                Role.ADMIN
        );

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/auth/register-adm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User created successfully"))
        ;

        User user = (User) userRepository.findByEmail(userDto.email());

        Assertions.assertEquals(userDto.email(), user.getEmail());
        Assertions.assertEquals(userDto.fullName(), user.getFullName());
        Assertions.assertEquals(userDto.role(), user.getRole());

    }

    @Test
    public void loginTest() throws Exception {

        AuthenticationDTO auth = new AuthenticationDTO(
                "john.doe@example.com",
                "securepassword123"
        );

        String jsonRequest = objectMapper.writeValueAsString(auth);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
        ;

    }

    @Test
    public void failLoginTest() throws Exception {

        AuthenticationDTO auth = new AuthenticationDTO(
                "mary.jane@example.com",
                "randomincorrectpassword"
        );

        String jsonRequest = objectMapper.writeValueAsString(auth);

        try{
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
            ;
        }catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }

    }


}
