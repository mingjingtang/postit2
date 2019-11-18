package com.example.usersapi.model.wrapper;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import java.util.List;

public class RoleWithUsers {

  private Long id;
  private String name;
  private List<User> users;

  public RoleWithUsers(UserRole userRole, List<User> users) {
    this.id = userRole.getId();
    this.name = userRole.getName();
    this.users = users;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }
}
