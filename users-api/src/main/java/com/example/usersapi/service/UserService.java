package com.example.usersapi.service;

import com.example.usersapi.model.User;
import java.util.List;

public interface UserService {

  public User signup(User user);

  public User login(User user);

  public List<User> listUsers();

  public Long findIdByUsername(String username);

}
