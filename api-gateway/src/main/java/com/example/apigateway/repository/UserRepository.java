package com.example.apigateway.repository;

import com.example.apigateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public User findUserByUsername(String username) {
    try {
      String sql = "SELECT * FROM users WHERE username = ?";
      return jdbcTemplate.queryForObject(sql, new Object[]{username},
          (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"), rs.getString("username"), rs.getString("password")));
    }catch (EmptyResultDataAccessException e){
      System.out.println("Entity record Not Found: username: " + username);
      return null;
    }
  }
}