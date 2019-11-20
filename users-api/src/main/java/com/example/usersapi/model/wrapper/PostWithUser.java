package com.example.usersapi.model.wrapper;

import com.example.usersapi.model.Post;
import com.example.usersapi.model.User;

public class PostWithUser {
    private Long postId;

    private String title;

    private String description;

    private User user;

    public PostWithUser(Post post, User user){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.user = user;
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
