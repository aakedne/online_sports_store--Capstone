package com.app.onlinesportstore.service;

import com.app.onlinesportstore.model.Product;
import com.app.onlinesportstore.repository.ProductRepository;
import com.app.onlinesportstore.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    /** This function is getting used to store the product in the database along with photos */
    public void saveProduct(Product product, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setPhoto(fileName);

        Product savedProduct = productRepository.save(product);

        String uploadDir = "product-photos/" + savedProduct.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    /** This function is getting used to fetch all the products from the database
     */
    public List<Product> getAllProducts() {
        log.info("Getting all the products from the database");
        return productRepository.getAllProducts();
    }

    /**
     * This function is getting used to fetch all the products that are not deleted.
     * Like which status is 0 means false.
     * That shows these products can be viewed to the frontend.
     */
    public List<Product> getAllUnDeletedProducts() {
        log.info("Getting all the un deleted products from the database");
        return productRepository.getAllUnDeletedProducts();
    }

    /** This function is getting used to get the product by its id. */
    public Product getProductById(Long productId) {
        log.info("Getting a specific product from the database against ID: {}", productId);
        return productRepository.getById(productId);
    }

    /** This function is getting used to delete the product by its id. */
    public void deleteProduct(Long productId) {
        log.info("Deleting a specific product from the database against ID: {}", productId);
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            product.get().setDeleteStatus(true);
            productRepository.save(product.get());
        }
    }
}
