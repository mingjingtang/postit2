package com.example.apigateway.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.apigateway.model.User;
import com.example.apigateway.model.UserRole;
import com.example.apigateway.repository.UserRepository;
import com.example.apigateway.repository.UserRoleRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  UserServiceImpl userService;

  @InjectMocks
  private User user;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserRoleRepository userRoleRepository;

  @Mock
  private PasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private UserRole userRole;

  List<UserRole> roles;

  @Before
  public void init() {
    user.setId(1L);
    user.setUsername("user1");
    user.setPassword("pwd1");
    user.setEmail("email1");

    userRole.setId(1L);
    userRole.setName("ROLE_USER");

    roles = new ArrayList<>();
    roles.add(userRole);
  }

  @Test
  public void loadUserByUsername_UserDetails_Success() {
    when(userRepository.findUserByUsername(anyString())).thenReturn(user);
    when(bCryptPasswordEncoder.encode(anyString())).thenReturn("password");
    when(userRoleRepository.findRolesByUserId(anyLong())).thenReturn(roles);

    UserDetails userDetails = userService.loadUserByUsername("username");

    assertEquals(user.getUsername(), userDetails.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_UserNotFound_UsernameNotFoundException() {
    when(userRepository.findUserByUsername(anyString())).thenReturn(null);
    userService.loadUserByUsername("username");
  }
}
