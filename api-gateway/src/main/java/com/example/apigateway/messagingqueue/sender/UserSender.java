package com.example.apigateway.messagingqueue.sender;

import com.example.apigateway.model.User;
import com.example.apigateway.model.UserRole;
import java.util.List;

public interface UserSender {

  public User findByUsername(String username);

  public List<UserRole> findRolesByUserId(Long userId);
}
