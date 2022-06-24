package com.app.onlinesportstore.repository;

import com.app.onlinesportstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /** This is a custom query to get the products by their id.
     */
    @Query(
            value = "SELECT * FROM products u WHERE u.id = ?1",
            nativeQuery = true)
    Product getById(Long productId);

    /** This is a custom query to get all the products from the database..
     */
    @Query(
            value = "SELECT * FROM products",
            nativeQuery = true)
    List<Product> getAllProducts();


    /** This is a custom query to get all the products that are not deleted.
     */
    @Query(
            value = "SELECT * FROM products where delete_status=0",
            nativeQuery = true)
    List<Product> getAllUnDeletedProducts();
}
