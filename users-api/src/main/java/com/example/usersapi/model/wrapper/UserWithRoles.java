package com.example.usersapi.model.wrapper;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import java.util.List;

public class UserWithRoles {

  private Long id;
  private String username;
  private List<UserRole> roles;

  public UserWithRoles(User user, List<UserRole> roles) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }
}
