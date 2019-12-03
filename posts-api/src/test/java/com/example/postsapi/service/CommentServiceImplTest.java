package com.example.postsapi.service;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.repository.CommentRepository;
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

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CommentServiceImplTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @Mock
  CommentRepository commentRepository;

  @Mock
  PostRepository postRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  PostUserRepository postUserRepository;

  @InjectMocks
  Comment comment;

  @InjectMocks
  CommentServiceImpl commentServiceImpl;

  @InjectMocks
  Post post;

  @InjectMocks
  User user;

  private PostWithUser postWithUser;

  private CommentWithDetails commentWithDetails;

  @Before
  public void init() {
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("post");

    user.setId(1L);
    user.setUsername("name");
    user.setEmail("name@gmail.com");

    postWithUser = new PostWithUser(post, user);

    // dummy comment
    comment.setCommentId(1L);
    comment.setText("comment");

    commentWithDetails = new CommentWithDetails(comment, user, postWithUser);
  }

  @Test
  public void createComment_Success() {
    when(commentRepository.createComment(anyString(), anyLong(), any())).thenReturn(comment);
    Comment resultComment = commentServiceImpl.createComment(user.getUsername(), 1L, comment);
    assertNotNull(resultComment);
    assertEquals(resultComment, comment);
  }

  @Test
  public void getCommentsByPostId_Success() throws JsonProcessingException {
    List<Comment> commentList = new ArrayList<>();
    commentList.add(comment);
    when(commentRepository.getCommentsByPostId(1L)).thenReturn(commentList);

    Map<Long, Long> commentIdToUserId = new HashMap<>();
    commentIdToUserId.put(1L, 1L);

    when(commentRepository.findCommentIdsToUserIds(anyList())).thenReturn(commentIdToUserId);
    when(commentRepository.findPostIdsByCommentIds(anyList())).thenReturn(commentIdToUserId);

    List<Post> posts = new ArrayList<>();
    posts.add(post);
    when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
    when(postRepository.findAllById(anyList())).thenReturn(posts);

    when(userRepository.findByUserId(anyLong())).thenReturn(user);

    when(postUserRepository.getUserIdByPostId(anyLong())).thenReturn(1L);

    when(userRepository.findByUserId(anyLong())).thenReturn(user);

    List<CommentWithDetails> listResult = commentServiceImpl.getCommentsByPostId(post.getPostId());

    List<CommentWithDetails> listResultExpect = new ArrayList<>();
    listResultExpect.add(commentWithDetails);

    assertNotNull(listResult);
    assertEquals(listResult.size(), listResultExpect.size());
    assertEquals(listResult.get(0).getId(), listResultExpect.get(0).getId());
  }
}
