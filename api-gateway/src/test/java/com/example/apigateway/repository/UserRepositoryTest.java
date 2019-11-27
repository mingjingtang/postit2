package com.example.apigateway.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.apigateway.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserRepositoryTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private UserRepository userRepository;

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private User user;

  @Before
  public void init() {
    User anotherUser = new User();
    user.setId(1L);
    user.setUsername("user1");
    user.setEmail("email1");
    user.setPassword("pwd1");
  }

  @Test
  public void findUserByUsername_User_Success() {
    when(jdbcTemplate.queryForObject(anyString(), any(), any(RowMapper.class))).thenReturn(user);
    User actualUser = userRepository.findUserByUsername("user1");
    assertEquals(user, actualUser);
    assertEquals(user.getEmail(), actualUser.getEmail());
  }

  @Test
  public void findUserByUsername_EmptyResultDataAccessException() {
    when(jdbcTemplate.queryForObject(anyString(), any(), any(RowMapper.class)))
        .thenThrow(EmptyResultDataAccessException.class);
    User actualUser = userRepository.findUserByUsername("user1");
    assertEquals(null, actualUser);
  }
}
