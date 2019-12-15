package com.example.apigateway.model;

import java.util.List;

public class UserWithRoles {

  private Long id;
  private String username;
  private List<UserRole> roles;

  public UserWithRoles() {

  }

  public UserWithRoles(User user, List<UserRole> roles) {
    this.setId(user.getId());
    this.setUsername(user.getUsername());
    this.setRoles(roles);
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
