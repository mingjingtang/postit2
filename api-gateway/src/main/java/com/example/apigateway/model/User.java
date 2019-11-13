package com.example.apigateway.model;

public class User {

  private Long id;

  private String username;

  private String email;

  private String password;

  public User() {
  }

  public User(Long id, String email, String username, String password) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.password = password;
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