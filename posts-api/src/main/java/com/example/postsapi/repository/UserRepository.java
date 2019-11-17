package com.example.postsapi.messagingqueue;

import com.example.postsapi.messagingqueue.sender.UserSender;
import com.example.postsapi.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  @Autowired
  private UserSender userSender;

  public Long findIdByUsername(String username) {
    return userSender.findIdByUsername(username);
  }

  public User findByUsername(String username) {
    return userSender.findByUsername(username);
  }

  public User findByUserId(Long userId) {
    return userSender.findByUserId(userId);
  }

  public List<User> findUsersByUserIds(List<Long> userIdList) throws JsonProcessingException {
    return userSender.findUsersByUserIds(userIdList);
  }
}
