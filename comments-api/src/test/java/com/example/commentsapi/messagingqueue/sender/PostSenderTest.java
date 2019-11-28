package com.example.commentsapi.messagingqueue.sender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

import com.example.commentsapi.model.Post;
import com.example.commentsapi.model.PostWithUser;
import com.example.commentsapi.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.amqp.core.AmqpTemplate;

public class PostSenderTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private PostSenderImpl postSender;

  @Mock
  private AmqpTemplate amqpTemplate;

  @InjectMocks
  private User user;

  @InjectMocks
  private Post post;

  private PostWithUser postWithUser;

  private ObjectMapper mapper = new ObjectMapper();

  @Before
  public void init() {
    user.setId(1L);
    user.setEmail("email1");
    user.setUsername("user1");
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("desp");
    postWithUser = new PostWithUser(post, user);
  }

  @Test
  public void findByPostId_PostWithUser_Success() throws IOException {
    String postJson = mapper.writeValueAsString(postWithUser);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(postJson);
    PostWithUser actualPostWithUser = postSender.findByPostId(1L);
    assertNotNull(actualPostWithUser);
    assertEquals(1l, (long)actualPostWithUser.getPostId());
  }

  @Test(expected = EntityNotFoundException.class)
  public void findByPostId_EntityNotFound_EntityNotFoundException() throws IOException {
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn("");
    postSender.findByPostId(1L);
  }

  @Test(expected = EntityNotFoundException.class)
  public void findByPostId_NullPost_EntityNotFoundException() throws IOException {
    String postJson = mapper.writeValueAsString(null);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(postJson);
    postSender.findByPostId(1L);
  }
}
