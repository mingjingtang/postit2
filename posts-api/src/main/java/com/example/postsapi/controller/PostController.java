package com.example.postsapi.controller;

import com.example.postsapi.model.Post;
import com.example.postsapi.service.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

  @Autowired
  private PostService postService;

  @PostMapping("/{username}")
  public Post createPost(@PathVariable String username, @RequestBody Post post) {

    return postService.createPost(username, post);
  }

  @DeleteMapping("/{postId}")
  public Long deletePost( @PathVariable Long postId) {

    return postService.deletePostByPostId(postId);
  }

  @GetMapping("/list")
  public List<Post> listPosts() {

    return postService.listPosts();
  }

//  @GetMapping("/{postId}/comment")
//  public List<Comment> getCommentsByPostId(@PathVariable Long postId)
//      throws EntityNotFoundException {
//
//    return postService.getCommentsByPostId(postId);
//  }
}
