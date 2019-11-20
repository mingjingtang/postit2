package com.example.apigateway.repository;

import com.example.apigateway.model.UserRole;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public UserRole findRoleById(Long roleId) {
    try {
      String sql = "SELECT * FROM roles WHERE id = ?";
      return jdbcTemplate.queryForObject(sql, new Object[]{roleId},
          (rs, rowNum) -> new UserRole(rs.getLong("id"), rs.getString("name")));
    } catch (EmptyResultDataAccessException e) {
      System.out.println("Entity record Not Found: username: " + roleId);
      return null;
    }
  }
}
