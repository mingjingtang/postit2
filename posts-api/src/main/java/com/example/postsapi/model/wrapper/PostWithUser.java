package com.example.postsapi.model.wrapper;

import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;

public class PostWithUser {

  private Long postId;

  private String title;

  private String description;

  private User user;

  public PostWithUser(Post post, User user) {
    this.setPostId(post.getPostId());
    this.setTitle(post.getTitle());
    this.setDescription(post.getDescription());
    this.setUser(user);
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
