package com.app.onlinesportstore.service;

import com.app.onlinesportstore.model.Order;
import com.app.onlinesportstore.model.Product;
import com.app.onlinesportstore.model.User;
import com.app.onlinesportstore.repository.OrderRepository;
import com.app.onlinesportstore.repository.ProductRepository;
import com.app.onlinesportstore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartService {

    HashMap<Long, Product> productHashMap = new HashMap<>();

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    /** This function is adding the products in the cart.
     */
    public boolean addToCart(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            if (productHashMap.containsKey(product.get().getId())) {
                log.info("This product is added to the cart: {}", product.get());
                return true;
            } else {
                productHashMap.put(product.get().getId(), product.get());
                return false;
            }
        } else {
            throw new RuntimeException("Product not found against this id:" + productId);
        }
    }

    /** This function is getting used to combine the things to view the cart.
     */
    public List<Product> viewCart() {
        List<Product> products = new ArrayList<>();
        productHashMap.forEach(
                (key, value) -> products.add(value));
        log.info("Current products in the cart: {}", products);
        return products;
    }

    /** This is a checkout function where all the checkout business logic is written.
     */
    public Order checkout() {
        List<Product> products = viewCart();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            Order order = new Order();
            order.setOrderDateTime(LocalDateTime.now());
            order.setProducts(products);
            double totalPrice = 0.0;
            for (Product p : products
            ) {
                totalPrice = totalPrice + p.getPrice();
            }
            order.setTotalPrice(totalPrice);
            Order savedOrder = orderRepository.save(order);
            user.get().getOrders().add(savedOrder);
            userRepository.save(user.get());
            productHashMap.clear();
            log.info("Final order of user whose username is {} and the ordered products are: {}", username, products);
            return savedOrder;
        } else {
            throw new RuntimeException("User doesn't exists in the database with this username");
        }
    }
}
