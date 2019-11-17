package com.example.usersapi.repository;

import com.example.usersapi.messagingqueue.sender.CommentSender;
import com.example.usersapi.model.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository {

  @Autowired
  private CommentSender commentSender;

  public List<Comment> findCommentsByUserId(Long userId) throws JsonProcessingException {
    return commentSender.findCommentsByUserId(userId);
  }

  public Map<Long, Long> findPostIdsByCommentIds(List<Long> commentIdList)
      throws JsonProcessingException {
    return commentSender.findPostIdsByCommentIds(commentIdList);
  }
}
