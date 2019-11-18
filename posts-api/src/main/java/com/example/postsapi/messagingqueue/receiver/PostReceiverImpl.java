package com.example.postsapi.messagingqueue.receiver;

import com.example.postsapi.model.Post;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.repository.PostUserRepository;
import com.example.postsapi.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostReceiverImpl implements PostReceiver{

  @Autowired
  private PostService postService;

  @Autowired
  private PostUserRepository postUserRepository;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  @RabbitListener(queuesToDeclare = @Queue("findByPostId"))
  public String handleMessage_findByPostId(String message) throws JsonProcessingException {
    System.out.println("received:" + message);
    String postJson = "";
    if (message.startsWith("findByPostId")) {
      Long postId = Long.parseLong(message.split(":")[1]);
      PostWithUser post = postService.findByPostId(postId);
      postJson = mapper.writeValueAsString(post);
      return postJson;
    }
    return "";
  }

  @Override
  @RabbitListener(queuesToDeclare = @Queue("findPostsByUserId"))
  public String handleMessage_findPostsByUserId(String message) throws JsonProcessingException {
    System.out.println("received:" + message);
    String postsJson = "";
    if (message.startsWith("findPostsByUserId")) {
      Long userId = Long.parseLong(message.split(":")[1]);
      List<Post> post = postService.findPostsByUserId(userId);
      postsJson = mapper.writeValueAsString(post);
      return postsJson;
    }
    return "";
  }

  @Override
  @RabbitListener(queuesToDeclare = @Queue("findPostsByPostIds"))
  public String handleMessage_findPostsByPostIds(String message) throws IOException {
    System.out.println("received:" + message);
    List<Long> postIdList = mapper.readValue(message, new TypeReference<List<Long>>() {
    });
    List<Post> posts = postService.findPostsByPostIds(postIdList);
    String postsJson = mapper.writeValueAsString(posts);
    return postsJson;
  }

  @Override
  @RabbitListener(queuesToDeclare = @Queue("findUserIdsByPostIds"))
  public String handleMessage_findUserIdsByPostIds(String message) throws IOException {
    System.out.println("received:" + message);
    List<Long> postIdList = mapper.readValue(message, new TypeReference<List<Long>>() {
    });
    Map<Long, Long> postIdToUserId = postUserRepository.findUserIdsByCommentIds(postIdList);
    String mapJson = mapper.writeValueAsString(postIdToUserId);
    return mapJson;
  }
}
