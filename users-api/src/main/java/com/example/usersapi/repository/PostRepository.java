package com.example.usersapi.repository;

import com.example.usersapi.messagingqueue.sender.PostSender;
import com.example.usersapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {

  @Autowired
  private PostSender postSender;

  public List<Post> findPostsByUserId(Long userId) {
    return postSender.findPostsByUserId(userId);
  }

  public List<Post> findPostsByPostIds(List<Long> postIdList) throws JsonProcessingException {
    return postSender.findPostsByPostIds(postIdList);
  }

  public Map<Long, Long> findUserIdsByPostIds(List<Long> postIdList)
      throws JsonProcessingException {
    return postSender.findUserIdsByPostIds(postIdList);
  }
}
