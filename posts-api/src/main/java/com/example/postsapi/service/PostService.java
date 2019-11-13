package com.example.postsapi.service;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import java.util.List;

public interface PostService {

  public Post createPost(String username, Post post);

  public Long deletePostByPostId(Long postId);

  public List<Post> listPosts();

  public Post updatePost(Long postId, Post post);

}
