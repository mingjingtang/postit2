package com.example.apigateway.repository;

import com.example.apigateway.messagingqueue.sender.UserSender;
import com.example.apigateway.model.UserRole;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleRepository {

  @Autowired
  private UserSender userSender;

  public List<UserRole> findRolesByUserId(Long userId) {
    return userSender.findRolesByUserId(userId);
  }
}
