package com.example.usersapi.model.wrapper;

import com.example.usersapi.model.Comment;
import com.example.usersapi.model.Post;
import com.example.usersapi.model.User;

public class CommentWithDetails {

  private Long id;
  private String text;
  private User user;
  private PostWithUser post;

  public CommentWithDetails(Comment comment, User commentAuthor, Post post, User postAuthor) {
    this.id = comment.getCommentId();
    this.text = comment.getText();
    this.user = commentAuthor;
    this.post = new PostWithUser(post, postAuthor);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public PostWithUser getPost() {
    return post;
  }

  public void setPost(PostWithUser post) {
    this.post = post;
  }
}
