package com.example.commentsapi.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.commentsapi.messagingqueue.sender.PostSenderImpl;
import com.example.commentsapi.model.Post;
import com.example.commentsapi.model.PostWithUser;
import com.example.commentsapi.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class PostRepositoryTest {
  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private PostRepository postRepository;

  @Mock
  private PostSenderImpl postSender;

  @InjectMocks
  private User user;

  @InjectMocks
  private Post post;

  @Before
  public void init(){
    user.setId(1L);
    user.setEmail("email1");
    user.setUsername("user1");
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("desp");
  }

  @Test
  public void findByPostId_PostWithUser_Success(){
    PostWithUser postWithUser = new PostWithUser(post, user);
    when(postSender.findByPostId(anyLong())).thenReturn(postWithUser);
    PostWithUser actualPostWithUser = postRepository.findByPostId(1L);
    assertEquals(1l, (long)actualPostWithUser.getPostId());
  }
}
