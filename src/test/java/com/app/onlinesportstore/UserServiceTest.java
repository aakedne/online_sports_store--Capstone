package com.app.onlinesportstore;

import com.app.onlinesportstore.model.Role;
import com.app.onlinesportstore.model.User;
import com.app.onlinesportstore.repository.UserRepository;
import com.app.onlinesportstore.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * In this test case we are testing our save user method in which we are saving our dummy user and checking
     * whether the response is same or not.
     */
    @Test
    public void saveUserTest() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@gmail.com")
                .username("test_user")
                .address("33rd street N, suite #3, Saint Cloud, Minnesota")
                .password(bCryptPasswordEncoder.encode("password"))
                .role(role)
                .build();
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userService.save(user));
    }

    /**
     * In this test case we are finding a user by its username.
     * First we are saving a user in our repository and then calling that function.
     * and checking whether this will return us that user or not.
     */
    @Test
    public void getUserByUsernameTest() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .email("test@gmail.com")
                .username("test_user")
                .address("33rd street N, suite #3, Saint Cloud, Minnesota")
                .password(bCryptPasswordEncoder.encode("password"))
                .role(role)
                .build();
        when(userRepository.findUserByUsername("test_user")).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUserByUsername("test_user").get());
    }
}
