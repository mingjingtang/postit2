package com.example.usersapi.messagingqueue.sender;

import com.example.usersapi.model.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;

public interface CommentSender {

  public List<Comment> findCommentsByUserId(Long userId) throws JsonProcessingException;

  public Map<Long, Long> findPostIdsByCommentIds(List<Long> commentIdList)
      throws JsonProcessingException;
}

