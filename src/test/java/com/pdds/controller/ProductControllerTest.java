package com.pdds.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdds.config.TestConfig;
import com.pdds.domain.Product;
import com.pdds.dto.ProductDTO;
import com.pdds.repository.ProductRepository;
import com.pdds.service.ProductService;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
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
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private String tokenAdm;
    private String tokenUser;

    @BeforeEach
    public void beforeEach() {

        testConfig.cleanDatabase();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build()
        ;
        objectMapper = new ObjectMapper();

        testConfig.cleanDatabase();

        tokenAdm = testConfig.getTokenAdm();
        tokenUser = testConfig.getTokenUser();

    }

    @Test
    public void createProductTest() throws Exception{

        ProductDTO product = new ProductDTO("Samsung Galaxy S25",1000.0,500);
        String jsonRequest = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Product created successfully"))
        ;

    }

    @Test
    public void failCreateProductIfUserIsNotAdminTest() throws Exception{

        ProductDTO product = new ProductDTO("Samsung Galaxy S25",1000.0,500);
        String jsonRequest = objectMapper.writeValueAsString(product);

        try {
            mockMvc.perform(post("/products/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenUser)
                            .content(jsonRequest))
                    .andExpect(status().isForbidden())
            ;
        }catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }

    }

    @Test
    public void getAllProductsTest() throws Exception{

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;

    }

    @Test
    public void findProductByIdTest() throws Exception{

        Product product = new Product("Samsung Galaxy S25",1000.0,500);
        Product createdProduct = productRepository.save(product);

        mockMvc.perform(get("/products/" + createdProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samsung Galaxy S25"))
                .andExpect(jsonPath("$.price").value(1000.0))
                .andExpect(jsonPath("$.stock").value(500))
        ;

    }

    @Test
    public void failFindProductWithWrongIdTest() throws Exception{

        mockMvc.perform(get("/products/1165498165498156498")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenUser))
                .andExpect(status().isNotFound())
        ;

    }

    @Test
    public void findProductByRegex() throws Exception{

        String text = "PlaySt";

        mockMvc.perform(get("/products?name=" + text)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;

    }

    @Test
    public void updateProductTest() throws Exception{

        Product product = new Product("Samsung Galaxy S25",1000.0,500);
        Product createdProduct = productRepository.save(product);

        ProductDTO dataToUpdateProduct = new ProductDTO("Xiaomi Redmi 15", 799.0, 1500);
        String jsonRequest = objectMapper.writeValueAsString(dataToUpdateProduct);

        mockMvc.perform(post("/products/update/" + createdProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product updated successfully"))
        ;

    }

    @Test
    public void filUpdateProductIfUserIsNotAdminTest() throws Exception{

        Product product = new Product("Samsung Galaxy S25",1000.0,500);
        Product createdProduct = productRepository.save(product);

        ProductDTO dataToUpdateProduct = new ProductDTO("Xiaomi Redmi 15", 799.0, 1500);
        String jsonRequest = objectMapper.writeValueAsString(dataToUpdateProduct);

        try{
            mockMvc.perform(post("/products/update/" + createdProduct.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenUser)
                            .content(jsonRequest))
                    .andExpect(status().isForbidden())
            ;
        }catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }
    }

    @Test
    public void deleteProductTest() throws Exception{

        Product product = new Product("Samsung Galaxy S25",1000.0,500);
        Product createdProduct = productRepository.save(product);

        mockMvc.perform(delete("/products/" + createdProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenAdm))
                .andExpect(status().isNoContent())
        ;

    }

    @Test
    public void failDeleteProductIfUserIsNotAdminTest() throws Exception{

        Product product = new Product("Samsung Galaxy S25",1000.0,500);
        Product createdProduct = productRepository.save(product);

        try {
            mockMvc.perform(delete("/products/" + createdProduct.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + tokenUser))
                    .andExpect(status().isForbidden())
            ;
        }catch (Exception e){
            Assertions.assertTrue(e instanceof ServletException);
        }

    }

    @Test
    public void addProductToCartTest() throws Exception{

        Product product = productService.findByRegex("iPhone").get(0);

        mockMvc.perform(get("/products/" + product.getId() + "/addCart?qt=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product added to the Shopping Cart"))
        ;

    }

}
