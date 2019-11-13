package com.example.postsapi.service;

import com.example.postsapi.model.Comment;

public interface CommentService {
    public Comment createComment(String username, Long postId, Comment comment);
}
