package com.example.commentsapi.controller;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.model.CommentWithDetails;
import com.example.commentsapi.service.CommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

  @Autowired
  private CommentService commentService;

  @PostMapping("/{postId}")
  @ApiOperation(value = "Create a comment for a post by a user", notes = "create comment", response = CommentWithDetails.class)
  public CommentWithDetails createComment(
      @ApiParam(value = "username", required = true) @RequestHeader("username") String username,
      @ApiParam(value = "post id", required = true) @PathVariable Long postId,
      @ApiParam(value = "comment body", required = true) @RequestBody Comment comment) {
    return commentService.createComment(username, postId, comment);
  }

  @DeleteMapping("/{commentId}")
  @ApiOperation(value = "Delete a comment by comment id", notes = "delete comment", response = Long.class)
  public Long deleteCommentById(
      @ApiParam(value = "username", required = true) @RequestHeader("username") String username,
      @ApiParam(value = "comment id", required = true) @PathVariable Long commentId) {
    return commentService.deleteComment(commentId);
  }
}
