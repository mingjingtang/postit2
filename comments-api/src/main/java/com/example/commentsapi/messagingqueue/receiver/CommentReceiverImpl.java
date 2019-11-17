package com.example.commentsapi.messagingqueue;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.repository.PostCommentRepository;
import com.example.commentsapi.repository.UserCommentRepository;
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
public class CommentListener {

  @Autowired
  private PostCommentRepository postCommentRepository;

  @Autowired
  private UserCommentRepository userCommentRepository;

  private ObjectMapper mapper = new ObjectMapper();

  @RabbitListener(queuesToDeclare = @Queue("findCommentsByUserId"))
  public String handleMessage_findCommentsByUserId(String message) throws JsonProcessingException {
    System.out.println("received:" + message);
    String userIdJson = "";
    if (message.startsWith("findCommentsByUserId")) {
      Long userId = Long.parseLong(message.split(":")[1]);
      List<Comment> commentList = userCommentRepository.findCommentsByUserId(userId);
      String commentsJson = mapper.writeValueAsString(commentList);
      return commentsJson;
    }
    return "";
  }

  @RabbitListener(queuesToDeclare = @Queue("findPostIdsByCommentIds"))
  public String handleMessage_findPostIdsByCommentIds(String message) throws IOException {
    System.out.println("received:" + message);
    List<Long> commentIdList = mapper.readValue(message, new TypeReference<List<Long>>() {
    });
    Map<Long, Long> commentIdToPostId = postCommentRepository
        .findPostIdsByCommentIds(commentIdList);
    String mapJson = mapper.writeValueAsString(commentIdToPostId);
    return mapJson;
  }

  @RabbitListener(queuesToDeclare = @Queue("getCommentsByPostId"))
  public String handleMessage_getCommentsByPostId(String message) throws IOException {
    System.out.println("received:" + message);
    if (message.startsWith("getCommentsByPostId")) {
      Long postId = Long.parseLong(message.split(":")[1]);
      List<Comment> commentList = postCommentRepository.findCommentsByPostId(postId);
      String commentsJson = mapper.writeValueAsString(commentList);
      return commentsJson;
    }
    return "";
  }

  @RabbitListener(queuesToDeclare = @Queue("findCommentIdsToUserIds"))
  public String handleMessage_findCommentIdsToUserIds(String message) throws IOException {
    System.out.println("received:" + message);
    List<Long> commentIdList = mapper.readValue(message, new TypeReference<List<Long>>() {
    });
    Map<Long, Long> commentIdToUserId = postCommentRepository
        .findUserIdsByCommentIds(commentIdList);
    String mapJson = mapper.writeValueAsString(commentIdToUserId);
    return mapJson;
  }
}
