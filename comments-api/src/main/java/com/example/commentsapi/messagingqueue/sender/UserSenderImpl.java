package com.example.commentsapi.messagingqueue.sender;

import com.example.commentsapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSenderImpl implements UserSender {

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public User findByUsername(String username) {
    String message = "findByUsername:" + username;
    System.out.println("Sending message: " + message);
    String userJson = (String) amqpTemplate.convertSendAndReceive("findByUsername", message);
    User user = null;
    try {
      user = mapper.readValue(userJson, User.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("user not found");
    }
    return user;
  }
}
