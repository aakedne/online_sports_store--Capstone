package com.app.onlinesportstore.service;

import com.app.onlinesportstore.model.Order;
import com.app.onlinesportstore.model.Product;
import com.app.onlinesportstore.model.User;
import com.app.onlinesportstore.repository.OrderRepository;
import com.app.onlinesportstore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    /** Function to get all the orders from the database.
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /** Function to get the specific user orders.
     */
    public List<Order> getOrdersByUser(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            log.info("Getting the orders from the database against user: {}", username);
            return user.get().getOrders();
        } else {
            throw new RuntimeException("User doesn't exists in the database against this username");
        }
    }

    /** Function to fetch the products from an order.*/
    public List<Product> getProductsByOrder(Long orderId) {
        log.info("Getting the products from the a specific order whose id is: {}", orderId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get().getProducts();
        } else {
            throw new RuntimeException("There is no order against this order Id: " + orderId);
        }
    }

    /** Function to fetch the customer from an order.*/
    public User getCustomerFromAnOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            List<User> users = userRepository.findAll();
            User foundUser = null;
            for (User user : users
            ) {
                for (Order ord : user.getOrders()
                ) {
                    if (ord.equals(order.get())) {
                        break;
                    }
                }
                foundUser = user;
            }
            log.info("Found the customer of this order: {}", foundUser);
            return foundUser;
        } else {
            throw new RuntimeException("There is no order against this order Id: " + orderId);
        }
    }
}
