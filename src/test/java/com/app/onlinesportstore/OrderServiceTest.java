package com.app.onlinesportstore;

import com.app.onlinesportstore.model.Order;
import com.app.onlinesportstore.model.Product;
import com.app.onlinesportstore.model.Role;
import com.app.onlinesportstore.model.User;
import com.app.onlinesportstore.repository.OrderRepository;
import com.app.onlinesportstore.repository.UserRepository;
import com.app.onlinesportstore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * In this case we are testing our getAllOrders function that is fetching all the orders from the database.
     */
    @Test
    public void getAllOrdersTest() {
        Product product = Product.builder()
                .id(1L)
                .name("Football")
                .description("The description of football")
                .photo("football.jpg")
                .price(5000.0)
                .build();

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = Order.builder()
                .id(1L)
                .orderDateTime(LocalDateTime.now())
                .products(productList)
                .totalPrice(5000.0)
                .build();

        List<Order> ordersList = new ArrayList<>();
        ordersList.add(order);
        when(orderRepository.findAll()).thenReturn(ordersList);
        assertEquals(ordersList, orderService.getAllOrders());
    }

    /**
     * In this case we are testing our getOrdersByUser where we are fetching orders of a specific user.
     */
    @Test
    public void getOrdersByUserTest() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@gmail.com")
                .username("test_user")
                .address("33rd street N, suite #3, Saint Cloud, Minnesota")
                .password(bCryptPasswordEncoder.encode("password"))
                .role(role)
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("Football")
                .description("The description of football")
                .photo("football.jpg")
                .price(5000.0)
                .build();

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = Order.builder()
                .id(1L)
                .orderDateTime(LocalDateTime.now())
                .products(productList)
                .totalPrice(5000.0)
                .build();

        List<Order> ordersList = new ArrayList<>();
        ordersList.add(order);

        user.setOrders(ordersList);

        when(userRepository.findUserByUsername("test_user")).thenReturn(Optional.of(user));
        assertEquals(user.getOrders(), orderService.getOrdersByUser("test_user"));
    }

    /**
     * In this case we are testing our getProductsByOrderTest method in which we are fetching all those products
     * that are ordered in a specific order.
     */
    @Test
    public void getProductsByOrderTest() {
        Product product = Product.builder()
                .id(1L)
                .name("Football")
                .description("The description of football")
                .photo("football.jpg")
                .price(5000.0)
                .build();

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = Order.builder()
                .id(1L)
                .orderDateTime(LocalDateTime.now())
                .products(productList)
                .totalPrice(5000.0)
                .build();

        order.setProducts(productList);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertEquals(productList, orderService.getProductsByOrder(1L));
    }


    /**
     * In this case we are testing our getCustomerFromAnOrder in which we are finding which customer
     * have placed that specific order.
     */
    @Test
    public void getCustomerFromAnOrderTest() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@gmail.com")
                .username("test_user")
                .address("33rd street N, suite #3, Saint Cloud, Minnesota")
                .password(bCryptPasswordEncoder.encode("password"))
                .role(role)
                .build();
        Product product = Product.builder()
                .id(1L)
                .name("Football")
                .description("The description of football")
                .photo("football.jpg")
                .price(5000.0)
                .build();

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Order order = Order.builder()
                .id(1L)
                .orderDateTime(LocalDateTime.now())
                .products(productList)
                .totalPrice(5000.0)
                .build();

        List<Order> ordersList = new ArrayList<>();
        ordersList.add(order);
        user.setOrders(ordersList);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findAll()).thenReturn(userList);
        assertEquals(user, orderService.getCustomerFromAnOrder(1L));
    }
}
