package com.example.apigateway.repository;

import com.example.apigateway.messagingqueue.sender.UserSender;
import com.example.apigateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  @Autowired
  private UserSender userSender;

  public User findUserByUsername(String username) {
    return userSender.findByUsername(username);
  }
}