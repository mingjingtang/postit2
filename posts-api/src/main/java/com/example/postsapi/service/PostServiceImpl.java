package com.example.postsapi.service;

import com.example.postsapi.repository.UserRepository;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.model.User;
import com.example.postsapi.repository.PostRepository;
import com.example.postsapi.repository.PostUserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostUserRepository postUserRepository;

  @Override
  public PostWithUser createPost(String username, Post post) {
    Post savedPost = postRepository.save(post);
    User user = userRepository.findByUsername(username);
    if (user == null) {
      return null;
    }
    postUserRepository.save(user.getId(), savedPost.getPostId());
    PostWithUser postWithUser = new PostWithUser(savedPost, user);
    return postWithUser;
  }

  @Override
  public Long deletePostByPostId(Long postId) {
    Post post = postRepository.findById(postId).orElse(null);
    if (post == null) {
      return 0L;
    }
    postRepository.delete(post);
    postUserRepository.deleteByPostId(postId);
    return post.getPostId();
  }

  @Override
  public List<PostWithUser> listPosts() throws JsonProcessingException {
    Iterable<Post> postIterable = postRepository.findAll();
    List<Post> postList = StreamSupport.stream(postIterable.spliterator(), false)
        .collect(Collectors.toList());
    List<Long> postIdList = postList.stream().map(Post::getPostId).collect(Collectors.toList());
    List<Long> userIdList = postUserRepository.findUserIdsByPostIds(postIdList);
    System.out.println("useridlist:" + userIdList);
    List<User> userList = userRepository.findUsersByUserIds(userIdList);
    System.out.println(userIdList);
    Map<Long, User> userMap = new HashMap<>();
    userList.forEach(user -> userMap.put(user.getId(), user));
    List<PostWithUser> postWithUserList = new ArrayList<>();
    for (int i = 0; i < postList.size(); i++) {
      postWithUserList.add(new PostWithUser(postList.get(i), userMap.get(userIdList.get(i))));
    }
    return postWithUserList;
  }

  @Override
  public Post updatePost(Long postId, Post post) {
    Post newPost = postRepository.findById(postId).get();
    newPost.setTitle(post.getTitle());
    newPost.setDescription(post.getDescription());
    postRepository.save(newPost);
    return newPost;
  }

  @Override
  public PostWithUser findByPostId(Long postId) {
    Post post = postRepository.findById(postId).orElse(null);
    if(post == null){
      return null;
    }
    Long userId = postUserRepository.getUserIdByPostId(postId);
    User user = userRepository.findByUserId(userId);
    PostWithUser postWithUser = new PostWithUser(post, user);
    return postWithUser;
  }

  @Override
  public List<Post> findPostsByUserId(Long userId) {
    return postUserRepository.findPostsByUserId(userId);
  }

  @Override
  public List<Post> findPostsByPostIds(List<Long> postIdList) {
    Iterable<Post> postIter = postRepository.findAllById(postIdList);
    List<Post> postList = StreamSupport.stream(postIter.spliterator(), false)
        .collect(Collectors.toList());
    return postList;
  }
}
