package com.example.postsapi.repository;

import com.example.postsapi.model.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostCommentRepository {

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  public List<Comment> getCommentsByPostId(Long postId) {
    String message = "getCommentsByPostId:" + postId;
    System.out.println("Sending message: " + message);
    String commentListJson = (String) amqpTemplate
        .convertSendAndReceive("getCommentsByPostId", message);

    List<Comment> commentList = new ArrayList<>();
    try {
      commentList = mapper.readValue(commentListJson, new TypeReference<List<Comment>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return commentList;
  }

  public Map<Long, Long> findCommentIdsToUserIds(List<Long> commentIdList)
      throws JsonProcessingException {
    if (commentIdList.size() == 0) {
      return new HashMap<Long, Long>();
    }
    String message = mapper.writeValueAsString(commentIdList);
    System.out.println("Sending message: " + message);
    String mapJson = (String) amqpTemplate
        .convertSendAndReceive("findCommentIdsToUserIds", message);
    Map<Long, Long> commentIdsToUserIds = new HashMap<>();
    try {
      commentIdsToUserIds = mapper.readValue(mapJson, new TypeReference<HashMap<Long, Long>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("commentid to userid not found");
    }
    return commentIdsToUserIds;
  }

  public Map<Long, Long> findPostIdsByCommentIds(List<Long> commentIdList)
      throws JsonProcessingException {
    if (commentIdList.size() == 0) {
      return new HashMap<Long, Long>();
    }
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

