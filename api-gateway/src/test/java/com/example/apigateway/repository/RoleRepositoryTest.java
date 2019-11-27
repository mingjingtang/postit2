package com.example.apigateway.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.apigateway.model.UserRole;
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

public class RoleRepositoryTest {
  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private RoleRepository roleRepository;

  @InjectMocks
  private UserRole userRole;

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Before
  public void init(){
    userRole.setId(1L);
    userRole.setName("ROLE_TEST");
  }

  @Test
  public void findRoleById_UserRole_Success(){
    when(jdbcTemplate.queryForObject(anyString(), any(), any(RowMapper.class))).thenReturn(userRole);

    UserRole actualUserRole = roleRepository.findRoleById(1L);
    assertEquals("ROLE_TEST", actualUserRole.getName());
    assertEquals((long)1L, (long)actualUserRole.getId());
  }

  @Test
  public void findRoleById_EmptyResultDataAccessException(){
    when(jdbcTemplate.queryForObject(anyString(), any(), any(RowMapper.class))).thenThrow(
        EmptyResultDataAccessException.class);

    UserRole actualUserRole = roleRepository.findRoleById(1L);
    assertNull(actualUserRole);
  }
}
