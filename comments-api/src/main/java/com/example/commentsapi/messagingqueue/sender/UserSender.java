package com.example.commentsapi.messagingqueue.sender;

import com.example.commentsapi.model.User;

public interface UserSender {

  public User findByUsername(String username);
}
