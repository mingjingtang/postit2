package com.example.commentsapi.repository;

import com.example.commentsapi.messagingqueue.sender.PostSender;
import com.example.commentsapi.model.PostWithUser;
import com.example.commentsapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {

  @Autowired
  private PostSender postSender;

  public PostWithUser findByPostId(Long postId) {
    return postSender.findByPostId(postId);
  }
}
