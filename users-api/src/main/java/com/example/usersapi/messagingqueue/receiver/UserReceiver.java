package com.example.usersapi.messagingqueue.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

public interface UserReceiver {

  public String handleMessage_findIdByUsername(String message);

  public String handleMessage_findByUsername(String message) throws JsonProcessingException;

  public String handleMessage_findByUserId(String message) throws JsonProcessingException;

  public String handleMessage_findUsersByUserIds(String message) throws IOException;
}

