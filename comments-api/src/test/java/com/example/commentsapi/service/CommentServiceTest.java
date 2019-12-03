package com.example.commentsapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.model.CommentWithDetails;
import com.example.commentsapi.model.Post;
import com.example.commentsapi.model.PostWithUser;
import com.example.commentsapi.model.User;
import com.example.commentsapi.repository.CommentPostRepository;
import com.example.commentsapi.repository.CommentRepository;
import com.example.commentsapi.repository.CommentUserRepository;
import com.example.commentsapi.repository.PostRepository;
import com.example.commentsapi.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.message.AuthException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CommentServiceTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private CommentServiceImpl commentService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PostRepository postRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private CommentPostRepository commentPostRepository;

  @Mock
  private CommentUserRepository commentUserRepository;

  @InjectMocks
  private User user;

  @InjectMocks
  private Post post;

  @InjectMocks
  private Comment comment;

  private PostWithUser postWithUser;
  private CommentWithDetails commentWithDetails;

  @Before
  public void init() {
    user.setId(1L);
    user.setEmail("email1@email.com");
    user.setUsername("user1");
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("description");
    postWithUser = new PostWithUser(post, user);
    comment.setCommentId(1L);
    comment.setText("comment");
    commentWithDetails = new CommentWithDetails();
    commentWithDetails.setId(comment.getCommentId());
    commentWithDetails.setText(comment.getText());
    commentWithDetails.setPost(postWithUser);
    commentWithDetails.setUser(user);
  }

  @Test
  public void createComment_CommentWithDetails_Success() {

    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(postRepository.findByPostId(anyLong())).thenReturn(postWithUser);
    when(commentRepository.save(any())).thenReturn(comment);
    when(commentPostRepository.save(anyLong(), anyLong())).thenReturn(1);
    when(commentUserRepository.save(anyLong(), anyLong())).thenReturn(1);

    CommentWithDetails actualCommentWithDetails = commentService
        .createComment("user1", 1L, comment);
    assertEquals(comment.getCommentId(), actualCommentWithDetails.getId());
  }

  @Test
  public void deleteComment_deletedId_Success() throws AuthException {
    List<Comment> comments = new ArrayList<>();
    comments.add(comment);
    when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
    when(commentUserRepository.findCommentsByUserId(anyLong())).thenReturn(comments);
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    Long actualCommentId = commentService.deleteComment("username", 3L);
    assertEquals(3l, (long) actualCommentId);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteComment_commentNotFound_EntityNotFoundException() throws AuthException {
    when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
    commentService.deleteComment("username", 1L);
  }

  @Test(expected = AuthException.class)
  public void deleteComment_unauthorizedDelete_AuthException() throws AuthException {
    List<Comment> comments = new ArrayList<>();
    when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
    when(commentUserRepository.findCommentsByUserId(anyLong())).thenReturn(comments);
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    commentService.deleteComment("username", 1L);
  }
}
