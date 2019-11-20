package com.example.commentsapi.model;


public class CommentWithDetails {

    private Long id;

    private String text;

    private User user;

    private PostWithUser post;

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
