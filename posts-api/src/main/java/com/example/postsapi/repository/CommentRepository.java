package com.example.postsapi.messagingqueue;

import com.example.postsapi.messagingqueue.sender.CommentSender;
import com.example.postsapi.model.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

  @Autowired
  private CommentSender commentSender;

  public Comment createComment(String username, Long postId, Comment text) {
    return commentSender.createComment(username, postId, text);
  }

  public List<Comment> getCommentsByPostId(Long postId) {
    return commentSender.getCommentsByPostId(postId);
  }

  public Map<Long, Long> findCommentIdsToUserIds(List<Long> commentIdList)
      throws JsonProcessingException {
    return commentSender.findCommentIdsToUserIds(commentIdList);
  }

  public Map<Long, Long> findPostIdsByCommentIds(List<Long> commentIdList)
      throws JsonProcessingException {
    return findPostIdsByCommentIds(commentIdList);
  }
}

