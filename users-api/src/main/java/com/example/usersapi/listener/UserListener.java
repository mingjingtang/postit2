package com.example.usersapi.listener;

import com.example.usersapi.model.User;
import com.example.usersapi.service.UserService;
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
}
