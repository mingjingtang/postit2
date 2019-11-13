package com.example.commentsapi.service;

import com.example.commentsapi.model.Comment;

public interface CommentService {
    public Comment createComment(Long postId, Comment comment);

    public Long deleteComment(Long commentId);
}
