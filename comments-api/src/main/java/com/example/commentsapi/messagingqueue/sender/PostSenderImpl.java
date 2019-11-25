package com.example.commentsapi.messagingqueue.sender;

import com.example.commentsapi.model.PostWithUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityNotFoundException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostSenderImpl implements PostSender {

  @Autowired
  private AmqpTemplate amqpTemplate;

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public PostWithUser findByPostId(Long postId) throws EntityNotFoundException {
    String message = "findByPostId:" + postId;
    System.out.println("Sending message: " + message);
    String postJson = (String) amqpTemplate.convertSendAndReceive("findByPostId", message);
    PostWithUser postWithUser = null;
    try {
      postWithUser = mapper.readValue(postJson, PostWithUser.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new EntityNotFoundException("post not found");
    }
    if (postWithUser == null) {
      throw new EntityNotFoundException("post not found");
    }
    return postWithUser;
  }
}
