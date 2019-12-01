package com.example.postsapi.messagingqueue.sender;

import com.example.postsapi.model.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;

public interface CommentSender {

  public Comment createComment(String username, Long postId, Comment text);

  public List<Comment> getCommentsByPostId(Long postId);

  public Map<Long, Long> findCommentIdsToUserIds(List<Long> commentIdList)
      throws JsonProcessingException;

  public Map<Long, Long> findPostIdsByCommentIds(List<Long> commentIdList)
      throws JsonProcessingException;
}
