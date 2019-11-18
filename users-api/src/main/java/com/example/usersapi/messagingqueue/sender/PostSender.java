package com.example.usersapi.messagingqueue.sender;

import com.example.usersapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;

public interface PostSender {

  public List<Post> findPostsByUserId(Long userId);

  public List<Post> findPostsByPostIds(List<Long> postIdList) throws JsonProcessingException;

  public Map<Long, Long> findUserIdsByPostIds(List<Long> postIdList) throws JsonProcessingException;
}
