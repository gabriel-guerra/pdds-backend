package com.pdds.service;

import com.pdds.config.TestConfig;
import com.pdds.domain.Product;
import com.pdds.dto.ProductDTO;
import com.pdds.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private TestConfig testConfig;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void beforeEach(){

        testConfig.cleanDatabase();

    }

    @Test
    public void serviceCreateProductTest() throws Exception{

        ProductDTO productDTO = new ProductDTO("Mouse Logitech G Pro", 79.99, 400, Arrays.asList("https://images.kabum.com.br/produtos/fotos/107333/mouse-gamer-sem-fio-logitech-g-pro-wireless-lightspeed-rgb-lightsync-ambidestro-6-botoes-programaveis-hero-25k-910-005271_1644501564_gg.jpg"));
        boolean create = productService.create(productDTO);

        Assertions.assertTrue(create);

    }

    @Test
    public void serviceGetAllProductsTest() throws Exception{

        List<Product> products = productService.getAll();
        Assertions.assertNotNull(products);

    }

    @Test
    public void serviceFindProductByIdTest() throws Exception{

        Product product = productRepository.save(new Product("Nvidia RTX 4070", 499.0, 350, Arrays.asList("https://www.nvidia.com/content/dam/en-zz/Solutions/geforce/news/geforce-rtx-4070/geforce-rtx-4070-newsfeed.png")));
        Optional<Product> opt = productService.findById(product.getId());

        Assertions.assertTrue(opt.isPresent());

        Product retrievedProduct = opt.get();

        Assertions.assertEquals(product.getName(), retrievedProduct.getName());
        Assertions.assertEquals(product.getPrice(), retrievedProduct.getPrice());
        Assertions.assertEquals(product.getStock(), retrievedProduct.getStock());
    }

    @Test
    public void serviceFindProductByRegexTest() throws Exception{

        String text = "Ninten";
        List<Product> products = productService.findByRegex(text);
        Assertions.assertNotNull(products);

    }

    @Test
    public void serviceUpdateProductTest() throws Exception{

        Product createdProduct = productRepository.save(new Product("Grand Theft Auto V PS4", 39.90, 250, Arrays.asList("https://m.media-amazon.com/images/I/81htlTqEckL._AC_UF1000,1000_QL80_.jpg")));
        ProductDTO dataToUpdate = new ProductDTO("EA FC 25 PS5", 59.99, 4000, Arrays.asList("https://m.media-amazon.com/images/I/6190AxEE7WL.jpg_BO30,255,255,255_UF900,850_SR1910,1000,0,C_PIRIOFOURANDHALF-medium,BottomLeft,30,-20_ZJPHNwYW4gZm9yZWdyb3VuZD0iIzU2NTk1OSIgZm9udD0iQW1hem9uRW1iZXIgNTAiID41ODg8L3NwYW4+,500,900,420,420,0,0_QL100_.jpg"));

        boolean update = productService.update(createdProduct.getId(), dataToUpdate);

        Product updatedProduct = productService.findById(createdProduct.getId()).get();

        Assertions.assertTrue(update);
        Assertions.assertEquals(dataToUpdate.name(), updatedProduct.getName());
        Assertions.assertEquals(dataToUpdate.price(), updatedProduct.getPrice());
        Assertions.assertEquals(dataToUpdate.stock(), updatedProduct.getStock());

    }

    @Test
    public void serviceDeleteProductTest() throws Exception{

        Product product = productRepository.save(new Product("Grand Theft Auto V PS4", 39.90, 250, Arrays.asList("https://m.media-amazon.com/images/I/81htlTqEckL._AC_UF1000,1000_QL80_.jpg")));

        boolean delete = productService.delete(product.getId());

        Assertions.assertTrue(delete);
        Assertions.assertFalse(productService.delete(product.getId()));

    }


}
