package com.example.postsapi.messagingqueue;

import com.example.postsapi.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class UserSenderImpl implements UserSender {

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public Long findIdByUsername(String username) {
    String message = "findIdByUsername:" + username;
    System.out.println("Sending message: " + message);
    String userIdStr = (String) amqpTemplate.convertSendAndReceive("findIdByUsername", message);
    if (userIdStr.length() == 0) {
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
    try {
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
    try {
      user = mapper.readValue(userJson, User.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("user not found");
    }
    return user;
  }

  @Override
  public List<User> findUsersByUserIds(List<Long> userIdList) throws JsonProcessingException {
    if (userIdList.size() == 0) {
      return new ArrayList<User>();
    }
    String userIdsJson = mapper.writeValueAsString(userIdList);
    System.out.println("Sending message: " + userIdsJson);
    String userListJson = (String) amqpTemplate
        .convertSendAndReceive("findUsersByUserIds", userIdsJson);
    System.out.println(userListJson);
    List<User> userList = new ArrayList<>();
    try {
      userList = mapper.readValue(userListJson, new TypeReference<List<User>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userList;
  }
}
