package com.example.usersapi.service;

import com.example.usersapi.model.User;
import com.example.usersapi.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public User signup(User user) {

    return userRepository.save(user);
  }

  @Override
  public User login(User user) {
    return userRepository.login(user.getEmail(), user.getPassword());
  }

  @Override
  public List<User> listUsers() {

    Iterable<User> userIter = userRepository.findAll();
    return StreamSupport.stream(userIter.spliterator(), false).collect(Collectors.toList());
  }
}
