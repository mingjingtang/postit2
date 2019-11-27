package com.example.apigateway.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

public class JwtUtilTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @InjectMocks
  JwtUtil jwtUtil;

  @Mock
  UserDetails userDetails;

  private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  @Before
  public void init() {

    jwtUtil.setSecret("test");
  }

  @Test
  public void generateToken_TokenString_Success() {

    when(userDetails.getUsername()).thenReturn("user1");
    String token = this.generateToken(userDetails);
    assertNotNull(token);
    assertTrue(token.length() > 0);
  }

  @Test
  public void getUsernameFromToken_Username_Success() {

    when(userDetails.getUsername()).thenReturn("user1");
    String token = this.generateToken(userDetails);
    String username = jwtUtil.getUsernameFromToken(token);
    assertEquals("user1", username);
  }

  @Test
  public void validateToken_Boolean_Success() {

    when(userDetails.getUsername()).thenReturn("user1");
    String token = this.generateToken(userDetails);
    Boolean isValid = jwtUtil.validateToken(token, userDetails);
    assertTrue(isValid);
  }

  @Test
  public void getExpirationDateFromToken_Date_Success() {

    when(userDetails.getUsername()).thenReturn("user1");

    String token = this.generateToken(userDetails);
    Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
    assertNotNull(expirationDate);

    Date now = new Date(System.currentTimeMillis());
    assertTrue(now.before(expirationDate));
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
