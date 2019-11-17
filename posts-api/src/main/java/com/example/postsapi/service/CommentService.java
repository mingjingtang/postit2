package com.example.postsapi.service;

import com.example.postsapi.model.Comment;

import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface CommentService {

  public Comment createComment(String username, Long postId, Comment comment);

  public List<CommentWithDetails> getCommentsByPostId(Long postId) throws JsonProcessingException;
}
