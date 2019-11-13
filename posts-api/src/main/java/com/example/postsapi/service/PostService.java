package com.example.postsapi.service;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.PostWithUser;
import java.util.List;

public interface PostService {

  public PostWithUser createPost(String username, Post post);

  public Long deletePostByPostId(Long postId);

  public List<Post> listPosts();

  public Post updatePost(Long postId, Post post);

}
