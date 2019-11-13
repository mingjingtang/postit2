package com.example.postsapi.repository;

import com.example.postsapi.model.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
    if(userIdStr.length() == 0){
      throw new RuntimeException("user not found");
    }
    Long userId = Long.parseLong(userIdStr);
    return userId;
  }

  @Override
  public User findByUsername(String username) {
    String message = "findByUsername:" + username;
    System.out.println("Sending message: " + message);
    String userJson = (String) amqpTemplate.convertSendAndReceive("findByUsername", message);
    User user = null;
    try{
      user = mapper.readValue(userJson, User.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("user not found");
    }
    return user;
  }

  @Override
  public User findByUserId(Long userId) {
    String message = "findByUserId:" + userId;
    System.out.println("Sending message: " + message);
    String userJson = (String) amqpTemplate.convertSendAndReceive("findByUserId", message);
    User user = null;
    try{
      user = mapper.readValue(userJson, User.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("user not found");
    }
    return user;
  }
}
