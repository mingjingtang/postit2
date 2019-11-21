package com.example.usersapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

public class UserServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    UserRepository userRepository;


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRoleService userRoleService;


    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private User user;

    @InjectMocks
    private UserRole userRole;


    @Before
    public void init() {

        user.setUsername("batman");
        user.setPassword("robin");

        userRole.setName("ROLE_ADMIN");
    }


    @Test
    public void login_UserNotFound() {

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        String token = userService.login(user);

        assertEquals(null, token);
    }

}
