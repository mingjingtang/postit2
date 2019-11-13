package com.example.usersapi.listener;

import com.example.usersapi.model.User;
import com.example.usersapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

  @Autowired
  private UserService userService;

  private ObjectMapper mapper = new ObjectMapper();

  @RabbitListener(queuesToDeclare = @Queue("findIdByUsername"))
  public String handleMessage_findIdByUsername(String message) {
    System.out.println("received:"+message);
    String userIdJson = "";
    if (message.startsWith("findIdByUsername")) {
      String username = message.split(":")[1];
      Long userId = userService.findIdByUsername(username);
      return userId.toString();
    }
    return "";
  }

  @RabbitListener(queuesToDeclare = @Queue("findByUsername"))
  public String handleMessage_findByUsername(String message) throws JsonProcessingException {
    System.out.println("received:"+message);
    String userJson = "";
    if (message.startsWith("findByUsername")) {
      String username = message.split(":")[1];
      User user = userService.findByUsername(username);
      userJson = mapper.writeValueAsString(user);
      return userJson;
    }
    return "";
  }

  @RabbitListener(queuesToDeclare = @Queue("findByUserId"))
  public String handleMessage_findByUserId(String message) throws JsonProcessingException {
    System.out.println("received:"+message);
    String userJson = "";
    if (message.startsWith("findByUserId")) {
      Long userId = Long.parseLong(message.split(":")[1]);
      User user = userService.findById(userId);
      userJson = mapper.writeValueAsString(user);
      return userJson;
    }
    return "";
  }
}
