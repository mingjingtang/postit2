package com.example.postsapi.messagingqueue;

import com.example.postsapi.model.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentSenderImpl implements CommentSender {

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public Comment createComment(String username, Long postId, Comment text) {
    String message = "addCommentToPost:" + username + ":" + postId + ":" + text;
    System.out.println("Sending message: " + message);
    String commentJson = (String) amqpTemplate.convertSendAndReceive("createComment", message);
    System.out.println(commentJson);
    Comment comment = null;
    try {
      comment = mapper.readValue(commentJson, Comment.class);
    } catch (Exception e) {
      System.out.println(e);
    }

    return comment;
  }

  @Override
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

  @Override
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

  @Override
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
