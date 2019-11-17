package com.example.commentsapi.messagingqueue.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface CommentReceiver {

  public String handleMessage_findCommentsByUserId(String message) throws JsonProcessingException;

  public String handleMessage_findPostIdsByCommentIds(String message) throws IOException;

  public String handleMessage_getCommentsByPostId(String message) throws IOException;

  public String handleMessage_findCommentIdsToUserIds(String message) throws IOException;
}
