package com.example.apigateway.repository;

import com.example.apigateway.model.UserRole;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private RoleRepository roleRepository;

  public List<UserRole> findRolesByUserId(Long userId) {
    String sql = "select role_id from user_role where user_id=" + userId;
    List<Long> roleIdList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("role_id"));

    List<UserRole> roles = new ArrayList<>();
    roleIdList.forEach(roleId -> roles.add(roleRepository.findRoleById(roleId)));
    return roles;
  }
}
