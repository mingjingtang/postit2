package com.example.commentsapi.controller;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}")
    public Comment createComment(@PathVariable Long postId, @RequestBody Comment comment){
        return commentService.createComment(postId, comment);
    }

    @DeleteMapping("/{commentId}")
    public Long deleteCommentById(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }
}
