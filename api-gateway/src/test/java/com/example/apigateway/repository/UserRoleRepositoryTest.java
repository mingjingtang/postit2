package com.example.apigateway.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.apigateway.model.UserRole;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserRoleRepositoryTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private UserRole userRole;

  @InjectMocks
  private UserRoleRepository userRoleRepository;

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private RoleRepository roleRepository;

  List<UserRole> roles;

  @Before
  public void init() {
    userRole.setId(1L);
    userRole.setName("ROLE_TEST");
  }

  @Test
  public void findRolesByUserId() {
    List<Long> roleIdList = new ArrayList<>();
    roleIdList.add(1L);
    when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(roleIdList);
    when(roleRepository.findRoleById(anyLong())).thenReturn(userRole);

    List<UserRole> actualRoles = userRoleRepository.findRolesByUserId(1L);
    assertEquals(userRole.getName(), actualRoles.get(0).getName());
  }
}
