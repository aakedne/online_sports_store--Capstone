package com.app.onlinesportstore.controller;

import com.app.onlinesportstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    OrderService orderService;

    /**
     * This api is getting used to show every customer which orders he recently placed.
     */
    @GetMapping("/orders")
    public String orders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("ordersList", orderService.getOrdersByUser(username));
        return "orders";
    }
}
