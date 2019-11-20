package com.example.commentsapi.messagingqueue.receiver;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.repository.CommentPostRepository;
import com.example.commentsapi.repository.CommentUserRepository;
import com.example.commentsapi.repository.PostRepository;
import com.example.commentsapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentReceiverImpl implements CommentReceiver {

  @Autowired
  private CommentUserRepository commentUserRepository;

  @Autowired
  private CommentPostRepository commentPostRepository;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  @RabbitListener(queuesToDeclare = @Queue("findCommentsByUserId"))
  public String handleMessage_findCommentsByUserId(String message) throws JsonProcessingException {
    System.out.println("received:" + message);
    String userIdJson = "";
    if (message.startsWith("findCommentsByUserId")) {
      Long userId = Long.parseLong(message.split(":")[1]);
      List<Comment> commentList = commentUserRepository.findCommentsByUserId(userId);
      String commentsJson = mapper.writeValueAsString(commentList);
      return commentsJson;
    }
    return "";
  }

  @Override
  @RabbitListener(queuesToDeclare = @Queue("findPostIdsByCommentIds"))
  public String handleMessage_findPostIdsByCommentIds(String message) throws IOException {
    System.out.println("received:" + message);
    List<Long> commentIdList = mapper.readValue(message, new TypeReference<List<Long>>() {
    });
    Map<Long, Long> commentIdToPostId = commentPostRepository
        .findPostIdsByCommentIds(commentIdList);
    String mapJson = mapper.writeValueAsString(commentIdToPostId);
    return mapJson;
  }

  @Override
  @RabbitListener(queuesToDeclare = @Queue("getCommentsByPostId"))
  public String handleMessage_getCommentsByPostId(String message) throws IOException {
    System.out.println("received:" + message);
    if (message.startsWith("getCommentsByPostId")) {
      Long postId = Long.parseLong(message.split(":")[1]);
      List<Comment> commentList = commentPostRepository.findCommentsByPostId(postId);
      String commentsJson = mapper.writeValueAsString(commentList);
      return commentsJson;
    }
    return "";
  }

  @Override
  @RabbitListener(queuesToDeclare = @Queue("findCommentIdsToUserIds"))
  public String handleMessage_findCommentIdsToUserIds(String message) throws IOException {
    System.out.println("received:" + message);
    List<Long> commentIdList = mapper.readValue(message, new TypeReference<List<Long>>() {
    });
    Map<Long, Long> commentIdToUserId = commentPostRepository
        .findUserIdsByCommentIds(commentIdList);
    String mapJson = mapper.writeValueAsString(commentIdToUserId);
    return mapJson;
  }
}
