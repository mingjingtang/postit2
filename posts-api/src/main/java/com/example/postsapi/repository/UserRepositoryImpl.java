package com.example.postsapi.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public Long findIdByUsername(String username) {
    String message = "findIdByUsername:" + username;
    System.out.println("Sending message: " + message);
    String userIdStr = (String) amqpTemplate.convertSendAndReceive("findIdByUsername", message);
    Long userId = Long.parseLong(userIdStr);
    if(userId == null){
      throw new RuntimeException("user not found");
    }
    return userId;
  }
}
