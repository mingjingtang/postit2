package com.example.usersapi.model.wrapper;

import com.example.usersapi.model.User;

public class UserWithPwd {

  private Long id;

  private String username;

  private String password;

  private String email;

  public UserWithPwd(User user) {
    id = user.getId();
    username = user.getUsername();
    password = user.getPassword();
    email = user.getEmail();
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
