package com.app.onlinesportstore.service;

import com.app.onlinesportstore.model.Role;
import com.app.onlinesportstore.model.User;
import com.app.onlinesportstore.repository.RoleRepository;
import com.app.onlinesportstore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder; //It's for password encryption

    /**
     * This is fetching the user by using his username
     * A query like where username= the value user will ask for
     */

    public Optional<User> getUserByUsername(String username) {
        log.info("Searching the user in the database against username: {}", username);
        return userRepository.findUserByUsername(username);
    }

    /** This function is saving the user in the database. */
    public User save(User user) {
        log.info("Saving a user in the database: {}", user);
        Optional<Role> existingRoleCheck = roleRepository.findByName("ROLE_USER");
        if (existingRoleCheck.isPresent()) {
            user.setRole(existingRoleCheck.get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            Role role = new Role();
            role.setName("ROLE_USER");
            Role customerRole = roleRepository.save(role);
            user.setRole(customerRole);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    /**
     * This function is the key function of spring security.
     * Spring security uses this function to find the user from the database and authenticate it.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Spring security authentication in progress against username: {}", username);
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    getAuthority(user.get())
            );
        } else {
            throw new RuntimeException("User doesn't exists");
        }
    }

    /**
     * This function is providing the role of the user that we have in the datatabase.
     */
    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorities;
    }
}
