package com.app.onlinesportstore.controller;

import com.app.onlinesportstore.service.OrderService;
import com.app.onlinesportstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;

    /**
     * This controller is rendering the view for the admin dashboard.
     */
    @GetMapping("/dashboard")
    public String productsList(Model model) {
        model.addAttribute("productsList", productService.getAllUnDeletedProducts());
        return "admin_dashboard";
    }

    /**
     * This controller is getting used to show all the orders that customers placed.
     */
    @GetMapping("/allOrders")
    public String getAllOrders(Model model) {
        model.addAttribute("ordersList", orderService.getAllOrders());
        return "orders";
    }
}
