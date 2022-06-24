package com.app.onlinesportstore;

import com.app.onlinesportstore.model.Role;
import com.app.onlinesportstore.model.User;
import com.app.onlinesportstore.repository.RoleRepository;
import com.app.onlinesportstore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class OnlineSportsStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineSportsStoreApplication.class, args);
    }

    /**
     * Here I have created a admin account which will be created when there is no admin account.
     * So, every time I'll check whether this admin account exists in our database or not, if not then add this.
     * This admin account will be the admin of all the products that will be storing the products and all other things.
     */
    @Bean
    CommandLineRunner run(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {
            //Here I am checking the role if that exists in the database
            //If that exists then check for the admin user if that exists too
            //If that doesn't exists then create the user.
            Optional<Role> existingRoleCheck = roleRepository.findByName("ROLE_ADMIN");
            if (existingRoleCheck.isPresent()) {
                Optional<User> optionalUser = userRepository.findUserByUsername("admin");
                if (!optionalUser.isPresent()) {

                    //It's for the user objection !!
                    // By this you don't have to use user.set etc
                    // By using this you can also create anonymous object !!
                    // Anonymous object means you don't have to store it in any object.
                    userRepository.save(User.builder()
                            .firstName("Admin")
                            .lastName("Account")
                            .address("Saint Cloud, Minnesota")
                            .email("admin@gmail.com")
                            .username("admin")
                            .password(passwordEncoder.encode("admin"))
                            .role(existingRoleCheck.get())
                            .build());
                }
            } else {
                //Here if the role is not present then a new role will be created
                //Then the user will be created and user will be assigned this role
                Role role = new Role();
                role.setName("ROLE_ADMIN");
                Role savedRole = roleRepository.save(role);
                Optional<User> optionalUser = userRepository.findUserByUsername("admin");
                if (!optionalUser.isPresent()) {
                    User user = User.builder()
                            .firstName("Admin")
                            .lastName("Account")
                            .address("Saint Cloud, Minnesota")
                            .email("admin@gmail.com")
                            .username("admin")
                            .password(passwordEncoder.encode("admin"))
                            .role(savedRole)
                            .build();
                    userRepository.save(user);
                }
            }
        };
    }
}
