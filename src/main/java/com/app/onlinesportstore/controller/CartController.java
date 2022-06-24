package com.app.onlinesportstore.controller;

import com.app.onlinesportstore.model.Order;
import com.app.onlinesportstore.model.Product;
import com.app.onlinesportstore.service.CartService;
import com.app.onlinesportstore.service.ProductService;
import com.app.onlinesportstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    /**
     * This controller is getting used to add the products in the cart.
     * It is accepting product id which we have to add in the cart.
     */
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable(name = "id") Long productId, Model model) {
        boolean alreadyPresent = cartService.addToCart(productId);
        if (alreadyPresent) {
            model.addAttribute("alreadyInCart", true);
        } else {
            model.addAttribute("addedToCart", true);
        }
        model.addAttribute("productsList", productService.getAllProducts());
        model.addAttribute("cartItems", cartService.viewCart());
        return "redirect:/cart/view";
    }

    /**
     * This controller is getting used to view the cart page.
     * It will show the cart page and all the products that are added in the cart yet.
     */
    @GetMapping("/view")
    public String viewCart(Model model) {
        List<Product> productsInCart = cartService.viewCart();
        Double totalPrice = 0.0;
        for (Product product : productsInCart
        ) {
            totalPrice = totalPrice + product.getPrice();
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartService.viewCart());
        return "shopping_cart";
    }


    /**
     * This is getting used for the final checkout where after finishing shopping,
     * When the user will click on checkout button, this api will get the hit.
     */
    @GetMapping("/checkout")
    public String checkout(Model model) {
        if(cartService.viewCart().isEmpty()){
            model.addAttribute("emptyCart",true);
            return "shopping_cart";
        }
        Order order = cartService.checkout();
        model.addAttribute("order", order);
        return "checkout";
    }

}
