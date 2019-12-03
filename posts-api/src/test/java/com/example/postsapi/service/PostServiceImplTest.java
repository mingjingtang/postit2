package com.example.postsapi.service;

import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.repository.PostRepository;
import com.example.postsapi.repository.PostUserRepository;
import com.example.postsapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.persistence.EntityNotFoundException;
import javax.security.auth.message.AuthException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PostServiceImplTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @Mock
  PostRepository postRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  PostUserRepository postUserRepository;

  @InjectMocks
  User user;

  @InjectMocks
  Post post;

  @InjectMocks
  PostServiceImpl postService;

  private PostWithUser postWithUser;

  List<Post> posts;
  List<Long> postIdList;
  Iterable<Post> postIter;

  @Before
  public void init() {
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("post");

    user.setId(1L);
    user.setUsername("name");
    user.setEmail("name@gmail.com");

    postWithUser = new PostWithUser(post, user);

    posts = new ArrayList<>();
    posts.add(post);

    postIdList = new ArrayList<>();
    postIdList.add(post.getPostId());
    postIter = posts;
  }

  @Test
  public void createPost_PostWithUser_Success() {
    when(postRepository.save(any())).thenReturn(post);
    when(userRepository.findByUsername(anyString())).thenReturn(user);

    PostWithUser actualPostWithUser = postService.createPost("name", post);
    assertEquals(postWithUser.getPostId(), actualPostWithUser.getPostId());
  }

  @Test
  public void deletePostByPostId_Long_Success() throws AuthException {
    when(postRepository.findById(anyLong())).thenReturn(java.util.Optional.of(post));
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(postUserRepository.getUserIdByPostId(anyLong())).thenReturn(user.getId());

    Long actualPostId = postService.deletePostByPostId("name", 1l);
    assertEquals(1l, (long) actualPostId);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deletePostByPostId_commentNotFound_EntityNotFoundException() throws AuthException {
    when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
    postService.deletePostByPostId("username", 1L);
  }

  @Test
  public void listPost_List_Success() throws JsonProcessingException {
    List<User> userList = new ArrayList<>();
    userList.add(user);

    when(postRepository.findAll()).thenReturn(postIter);
    when(postUserRepository.findUserIdsByPostIds(anyList())).thenReturn(postIdList);
    when(userRepository.findUsersByUserIds(anyList())).thenReturn(userList);

    List<PostWithUser> actualPostWithUserList = postService.listPosts();
    assertNotNull(actualPostWithUserList);
    List<PostWithUser> expectPostWithUserList = new ArrayList<>();
    expectPostWithUserList.add(postWithUser);

    assertEquals(expectPostWithUserList.size(), actualPostWithUserList.size());
    assertEquals(expectPostWithUserList.get(0).getPostId(),
        actualPostWithUserList.get(0).getPostId());
  }

  @Test
  public void findByPostId_PostWithUser_Success() {
    when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
    when(postUserRepository.getUserIdByPostId(anyLong())).thenReturn(user.getId());
    when(userRepository.findByUserId(anyLong())).thenReturn(user);
    PostWithUser actualPostWithUser = postService.findByPostId(1l);
    assertNotNull(actualPostWithUser);
    assertEquals(postWithUser.getPostId(), actualPostWithUser.getPostId());
  }

  @Test
  public void findPostByUserId_List_Success() {
    when(postUserRepository.findPostsByUserId(anyLong())).thenReturn(posts);
    List<Post> actualPostList = postService.findPostsByUserId(user.getId());
    assertNotNull(actualPostList);
    assertEquals(posts, actualPostList);
  }

  @Test
  public void findPostByPostId_List_Success() {
    when(postRepository.findAllById(anyList())).thenReturn(postIter);
    List<Post> actualPosts = postService.findPostsByPostIds(postIdList);
    assertNotNull(actualPosts);
    assertEquals(posts, actualPosts);
  }
}
