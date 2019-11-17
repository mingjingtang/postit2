package com.example.postsapi.controller;

import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.service.CommentService;
import com.example.postsapi.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private CommentService commentService;

  @PostMapping("/")
  public PostWithUser createPost(@RequestHeader("username") String username,
      @RequestBody Post post) {
    return postService.createPost(username, post);
  }

  @DeleteMapping("/{postId}")
  public Long deletePost(@RequestHeader("username") String username, @PathVariable Long postId) {

    return postService.deletePostByPostId(postId);
  }

  @GetMapping("/list")
  public List<PostWithUser> listPosts() throws JsonProcessingException {
    return postService.listPosts();
  }

  @PutMapping("/{postId}")
  public Post updatePost(@PathVariable Long postId, @RequestBody Post post) {
    return postService.updatePost(postId, post);
  }

  @GetMapping("{postId}/comment")
  public List<CommentWithDetails> getComments(@PathVariable Long postId)
      throws JsonProcessingException {
    return commentService.getCommentsByPostId(postId);
  }
}
