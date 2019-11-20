package com.example.usersapi.messagingqueue.sender;

import com.example.usersapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostSenderImpl implements PostSender{

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<Post> findPostsByUserId(Long userId) {
    String message = "findPostsByUserId:" + userId;
    System.out.println("Sending message: " + message);
    String postsJson = (String) amqpTemplate.convertSendAndReceive("findPostsByUserId", message);
    List<Post> postList = new ArrayList<>();
    try {
      postList = mapper.readValue(postsJson, new TypeReference<List<Post>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("postList not found");
    }
    return postList;
  }

  @Override
  public List<Post> findPostsByPostIds(List<Long> postIdList) throws JsonProcessingException {
    if(postIdList.size() == 0){
      return new ArrayList<Post>();
    }
    String message = mapper.writeValueAsString(postIdList);
    System.out.println("Sending message: " + message);
    String postsJson = (String) amqpTemplate.convertSendAndReceive("findPostsByPostIds", message);
    List<Post> postList = new ArrayList<>();
    try {
      postList = mapper.readValue(postsJson, new TypeReference<List<Post>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("postList not found");
    }
    return postList;
  }

  @Override
  public Map<Long, Long> findUserIdsByPostIds(List<Long> postIdList)
      throws JsonProcessingException {
    String message = mapper.writeValueAsString(postIdList);
    System.out.println("Sending message: " + message);
    String mapJson = (String) amqpTemplate.convertSendAndReceive("findUserIdsByPostIds", message);
    Map<Long, Long> postIdToUserId = new HashMap<>();
    try {
      postIdToUserId = mapper.readValue(mapJson, new TypeReference<HashMap<Long, Long>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("postList not found");
    }
    return postIdToUserId;
  }
}
