package com.example.postsapi.service;

import com.example.postsapi.model.Comment;

import java.util.List;

public interface CommentService {
    public Comment createComment(String username, Long postId, Comment comment);

    public List<Comment> getComments(Long postId);
}
