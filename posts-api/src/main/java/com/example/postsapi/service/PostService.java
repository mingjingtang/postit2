package com.example.postsapi.service;

import com.example.postsapi.model.Post;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import javax.security.auth.message.AuthException;

public interface PostService {

  public PostWithUser createPost(String username, Post post);

  public Long deletePostByPostId(String username, Long postId) throws AuthException;

  public List<PostWithUser> listPosts() throws JsonProcessingException;

  public PostWithUser findByPostId(Long postId);

  public List<Post> findPostsByUserId(Long userId);

  public List<Post> findPostsByPostIds(List<Long> postIdList);
}
