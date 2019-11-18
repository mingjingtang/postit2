package com.example.commentsapi.service;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.model.CommentWithDetails;

public interface CommentService {

  public CommentWithDetails createComment(String username, Long postId, Comment comment);

  public Long deleteComment(Long commentId);
}
