package com.example.postsapi.service;

import com.example.postsapi.model.Post;
import com.example.postsapi.repository.PostRepository;
import com.example.postsapi.repository.PostUserRepository;
import com.example.postsapi.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostUserRepository postUserRepository;

  @Override
  public Post createPost(String username, Post post) {
    Post savedPost = postRepository.save(post);
    Long userId = userRepository.findIdByUsername(username);
    if (userId == 0) {
      return null;
    }
    postUserRepository.save(userId, savedPost.getPostId());
    return savedPost;
  }

  @Override
  public Long deletePostByPostId(Long postId) {
    Post post = postRepository.findById(postId).orElse(null);
    if (post == null) {
      return 0L;
    }
    postRepository.delete(post);
    return post.getPostId();
  }

  @Override
  public List<Post> listPosts() {
    Iterable<Post> postIterable = postRepository.findAll();
    return StreamSupport.stream(postIterable.spliterator(), false).collect(Collectors.toList());
  }
}
