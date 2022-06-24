package com.app.onlinesportstore.controller;

import com.app.onlinesportstore.model.Product;
import com.app.onlinesportstore.service.CartService;
import com.app.onlinesportstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    /**
     * This api will be getting used view the page to add new products
     *
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String getProductUploadForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "upload_product";
    }

    /**
     * This api will be getting used to create/add new products in the database.
     *
     * @param product
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/save")
    public String saveUser(Product product, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        productService.saveProduct(product, multipartFile);
        return "redirect:/admin/dashboard";
    }

    /**
     * This api will be getting used to view all the products that are in the database.
     *
     * @param model
     * @return
     */
    @GetMapping("/all")
    public String productsList(Model model) {
        model.addAttribute("productsList", productService.getAllUnDeletedProducts());
        model.addAttribute("cartItems", cartService.viewCart());
        return "customer-dashboard";
    }

    /**
     * This controller will show us the details of a single product.
     */
    @GetMapping("/getById/{id}")
    public String getProductById(Model model, @PathVariable(name = "id") Long productId) {
        model.addAttribute("product", productService.getProductById(productId));
        return "single_product";
    }

    /**
     * This is getting used to delete a product from the frontend.
     */
    @GetMapping("/deleteById/{id}")
    public String deleteProductById(@PathVariable(name = "id") Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/admin/dashboard";
    }

    /**
     * This is getting used to update a product in the database.
     */
    @GetMapping("/update/{id}")
    public String updateProduct(Model model, @PathVariable(name = "id") Long productId) {
        model.addAttribute("productId", productId);
        model.addAttribute("product", productService.getProductById(productId));
        return "update_product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable(name = "id") Long productId, Product product, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        product.setId(productId);
        productService.saveProduct(product, multipartFile);
        return "redirect:/admin/dashboard";
    }
}
