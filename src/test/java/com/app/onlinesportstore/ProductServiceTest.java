package com.app.onlinesportstore;

import com.app.onlinesportstore.model.Product;
import com.app.onlinesportstore.repository.ProductRepository;
import com.app.onlinesportstore.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    /**
     * In this case we are testing our getProductById method in which we are finding a product
     * against a specific product ID.
     */
    @Test
    public void getProductByIdTest() {
        Product product = Product.builder()
                .id(1L)
                .name("Football")
                .description("The description of football")
                .photo("football.jpg")
                .price(5000.0)
                .build();
        when(productRepository.getById(1L)).thenReturn(product);
        assertEquals(product, productService.getProductById(1L));
    }

    /**
     * In this case we are testing our getAllProducts method in which we are fetching all the products from the database.
     */
    @Test
    public void getAllProductsTest() {
        Product product = Product.builder()
                .id(1L)
                .name("Football")
                .description("The description of football")
                .photo("football.jpg")
                .price(5000.0)
                .build();

        List<Product> productList = new ArrayList<>();

        productList.add(product);
        when(productRepository.getAllProducts()).thenReturn(productList);
        assertEquals(productList, productService.getAllProducts());
    }
}
