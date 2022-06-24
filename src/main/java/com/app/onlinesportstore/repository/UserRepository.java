package com.app.onlinesportstore.repository;

import com.app.onlinesportstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /** This is a custom query to get the user from the database against its username
     */
    @Query(
            value = "SELECT * FROM users u WHERE u.username = ?1",
            nativeQuery = true)
    Optional<User> findUserByUsername(String username);
}
