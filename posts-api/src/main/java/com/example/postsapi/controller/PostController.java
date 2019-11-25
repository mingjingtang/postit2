package com.example.postsapi.controller;

import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.service.CommentService;
import com.example.postsapi.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.security.auth.message.AuthException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private CommentService commentService;

  @PostMapping("/")
  @ApiOperation(value = "Create a post by a user", notes = "create post", response = PostWithUser.class)
  public PostWithUser createPost(
      @ApiParam(value = "username", required = true) @RequestHeader("username") String username,
      @ApiParam(value = "post body", required = true) @Valid @RequestBody Post post) {
    return postService.createPost(username, post);
  }

  @DeleteMapping("/{postId}")
  @ApiOperation(value = "Delete a post by post id", notes = "delete post", response = Long.class)
  public Long deletePost(
      @ApiParam(value = "username", required = true) @RequestHeader("username") String username,
      @ApiParam(value = "post id", required = true) @PathVariable Long postId)
      throws AuthException {

    return postService.deletePostByPostId(username, postId);
  }

  @GetMapping("/list")
  @ApiOperation(value = "list all posts", notes = "list posts", response = PostWithUser.class, responseContainer = "List")
  public List<PostWithUser> listPosts() throws JsonProcessingException {
    return postService.listPosts();
  }

  @GetMapping("{postId}/comment")
  @ApiOperation(value = "get comments for a post", notes = "get comments by post id", response = CommentWithDetails.class, responseContainer = "List")
  public List<CommentWithDetails> getComments(
      @ApiParam(value = "post id", required = true) @PathVariable Long postId)
      throws JsonProcessingException {
    return commentService.getCommentsByPostId(postId);
  }
}
