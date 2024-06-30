package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void testCreateUserHappyPath() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User createdUser = response.getBody();
        assertNotNull(createdUser);
        assertEquals(0, createdUser.getId());
        assertEquals("testUser", createdUser.getUsername());
        assertEquals("hashedPassword", createdUser.getPassword());
    }

    @Test
    public void testFindUserById() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> createUserResponse = userController.createUser(createUserRequest);
        User createdUser = createUserResponse.getBody();
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(createdUser));

        ResponseEntity<User> findUserResponse = userController.findById(0L);
        User foundUser = findUserResponse.getBody();

        assertNotNull(foundUser);
        assertEquals(0, foundUser.getId());
        assertEquals("testUser", foundUser.getUsername());
        assertEquals("hashedPassword", foundUser.getPassword());
    }

    @Test
    public void testFindUserByUsername() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> createUserResponse = userController.createUser(createUserRequest);
        User createdUser = createUserResponse.getBody();
        when(userRepository.findByUsername("testUser")).thenReturn(createdUser);

        ResponseEntity<User> findUserResponse = userController.findByUserName("testUser");
        User foundUser = findUserResponse.getBody();

        assertNotNull(foundUser);
        assertEquals(0, foundUser.getId());
        assertEquals("testUser", foundUser.getUsername());
        assertEquals("hashedPassword", foundUser.getPassword());
    }
}