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

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void get_user_by_username_happy_path() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("test");

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<User> response =  userController.findByUserName("test");

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", u.getUsername());
        assertEquals(1, user.getId());

    }

    @Test
    public void get_user_by_username_fail() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("test");

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<User> response =  userController.findByUserName("testfail");

        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void get_user_by_id_happy_path() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("test");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response =  userController.findById(1L);

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", u.getUsername());
        assertEquals(1, user.getId());

    }

    @Test
    public void get_user_by_id_fail() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("test");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response =  userController.findById(2L);

        assertEquals(404, response.getStatusCodeValue());

    }

}
