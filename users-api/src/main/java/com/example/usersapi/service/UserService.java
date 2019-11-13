package com.example.usersapi.service;

import com.example.usersapi.model.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  public String signup(User user);

  public String login(User user);

  public List<User> listUsers();

  public Long findIdByUsername(String username);

  public User findByEmail(String email);

}
