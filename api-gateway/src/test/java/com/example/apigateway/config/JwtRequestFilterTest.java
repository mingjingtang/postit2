package com.example.apigateway.config;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.apigateway.controller.TestController;
import com.example.apigateway.model.User;
import com.example.apigateway.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtRequestFilterTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  private MockMvc mockMvc;

  @InjectMocks
  private TestController testController;

  @InjectMocks
  JwtRequestFilter jwtRequestFilter;

  @Mock
  private UserService userService;

  @Mock
  JwtUtil jwtUtil;

  @InjectMocks
  private User user;

  private PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  private UserDetails userDetails;

  private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  @Before
  public void init() {

    mockMvc = MockMvcBuilders.standaloneSetup(testController).addFilter(jwtRequestFilter).build();
  }

  @Test
  public void jwtRequiredRequest_JwtRequestFilter_EmptyToken() throws Exception {

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/unittest");

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    assertEquals("helloworld", result.getResponse().getContentAsString());
  }

  @Test
  public void jwtRequiredRequest_JwtRequestFilter_IllegalBearerToken() throws Exception {

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auth")
        .header("Authorization", "Bearer 12345678910");

    when(jwtUtil.getUsernameFromToken(anyString())).thenThrow(new IllegalArgumentException());

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    assertEquals("helloauth", result.getResponse().getContentAsString());
  }

  @Test
  public void jwtRequiredRequest_JwtRequestFilter_ExpiredBearerToken() throws Exception {

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auth")
        .header("Authorization", "Bearer 12345678910");

    when(jwtUtil.getUsernameFromToken(anyString()))
        .thenThrow(new ExpiredJwtException(null, null, null));

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    assertEquals("helloauth", result.getResponse().getContentAsString());
  }

  @Test
  public void test() throws Exception {
    user.setId(1L);
    user.setEmail("email1");
    user.setUsername("user1");
    user.setPassword("pwd1");

    userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
        bCryptPasswordEncoder.encode(user.getPassword()), true, true, true, true,
        new ArrayList<>());
    String jwt = this.generateToken(userDetails);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/auth")
        .header("Authorization", "Bearer " + jwt);

    when(userService.loadUserByUsername(anyString())).thenReturn(userDetails);
    when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("user1");
    when(jwtUtil.validateToken(anyString(), any())).thenReturn(true);

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    assertEquals("helloauth", result.getResponse().getContentAsString());
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, "test").compact();
  }
}
