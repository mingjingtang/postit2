package com.example.apigateway.messagingqueue.sender;

import com.example.apigateway.model.User;
import com.example.apigateway.model.UserRole;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
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
    String userJson = (String) amqpTemplate.convertSendAndReceive("findByUsernameWithPassword", message);
    System.out.println(userJson);
    User user = null;
    try {
      user = mapper.readValue(userJson, User.class);
    } catch (Exception e){
      e.printStackTrace();
      throw new RuntimeException("user not found");
    }
    return user;
  }

  @Override
  public List<UserRole> findRolesByUserId(Long userId) {
    String message = "findRolesByUserId:" + userId;
    System.out.println("Sending message: " + message);
    String userJson = (String) amqpTemplate.convertSendAndReceive("findRolesByUserId", message);
    List<UserRole> roles = null;
    try {
      roles = mapper.readValue(userJson, new TypeReference<List<UserRole>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("user roles not found");
    }
    return roles;
  }
}
