package com.example.usersapi.repository;

import com.example.usersapi.model.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserCommentRepository {

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  public List<Comment> findCommentsByUserId(Long userId) throws JsonProcessingException {

    String message = "findCommentsByUserId:" + userId;
    System.out.println("Sending message: " + message);
    String mapJson = (String) amqpTemplate.convertSendAndReceive("findCommentsByUserId", message);
    List<Comment> commentList = new ArrayList<>();
    try {
      commentList = mapper.readValue(mapJson, new TypeReference<List<Comment>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("postList not found");
    }
    return commentList;
  }

  public Map<Long, Long> findPostIdsByCommentIds(List<Long> commentIdList)
      throws JsonProcessingException {
    String message = mapper.writeValueAsString(commentIdList);
    System.out.println("Sending message: " + message);
    String mapJson = (String) amqpTemplate
        .convertSendAndReceive("findPostIdsByCommentIds", message);
    Map<Long, Long> commentIdToPostId = new HashMap<>();
    try {
      commentIdToPostId = mapper.readValue(mapJson, new TypeReference<HashMap<Long, Long>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("postList not found");
    }
    return commentIdToPostId;
  }
}
