package com.example.usersapi.repository;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import java.util.ArrayList;
import java.util.List;
import javax.management.relation.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleRepository {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public int save(Long userId, Long roleId) {
    return jdbcTemplate
        .update("insert into user_role (user_id, role_id) " + "values(?,?)", userId, roleId);
  }

  public List<UserRole> findRolesByUserId(Long userId) {
    String sql = "select role_id from user_role where user_id=" + userId;
    List<Long> roleIdList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("role_id"));

    List<UserRole> roles = new ArrayList<>();
    roleIdList.forEach(roleId -> roles.add(roleRepository.findById(roleId).orElse(null)));
    return roles;
  }

  public List<User> findUserByRoleId(Long roleId) {
    String sql = "select user_id from user_role where role_id=" + roleId;
    List<Long> userIdList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"));

    List<User> users = new ArrayList<>();
    userIdList.forEach(userId -> users.add(userRepository.findById(userId).orElse(null)));
    return users;
  }

  public int delete(Long userId, Long roleId) {
    return jdbcTemplate
        .update("DELETE FROM user_role WHERE user_id = ? AND role_id = ?", userId, roleId);
  }
}
