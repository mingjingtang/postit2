package com.example.postsapi.messagingqueue.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

public interface PostReceiver {

  public String handleMessage_findByPostId(String message) throws JsonProcessingException;

  public String handleMessage_findPostsByUserId(String message) throws JsonProcessingException;

  public String handleMessage_findPostsByPostIds(String message) throws IOException;

  public String handleMessage_findUserIdsByPostIds(String message) throws IOException;
}
