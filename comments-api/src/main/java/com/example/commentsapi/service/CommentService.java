package com.example.commentsapi.service;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.model.CommentWithDetails;
import javax.security.auth.message.AuthException;

public interface CommentService {

  public CommentWithDetails createComment(String username, Long postId, Comment comment);

  public Long deleteComment(String username, Long commentId) throws AuthException;
}
