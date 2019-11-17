package com.example.postsapi.messagingqueue;

import com.example.postsapi.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface UserSender {

  public Long findIdByUsername(String username);

  public User findByUsername(String username);

  public User findByUserId(Long userId);

  public List<User> findUsersByUserIds(List<Long> userIdList) throws JsonProcessingException;
}
