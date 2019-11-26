package com.example.usersapi.messagingqueue.sender;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.usersapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.amqp.core.AmqpTemplate;

public class PostSenderTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private PostSenderImpl postSender;

  @InjectMocks
  private Post post;

  @Mock
  private AmqpTemplate amqpTemplate;

  @Mock
  private ObjectMapper mapper = new ObjectMapper();

  private List<Post> posts;

  @Before
  public void init() {
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("description");

    posts = new ArrayList<>();
    posts.add(post);
  }

  @Test
  public void findPostsByUserId_ListOfPosts_Success() throws IOException {
    String postsJson = (new ObjectMapper()).writeValueAsString(posts);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(postsJson);
    when(mapper.writeValueAsString(any())).thenReturn("message");
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(posts);

    List<Post> actualPosts = postSender.findPostsByUserId(1L);
    assertEquals(posts.size(), actualPosts.size());
    assertEquals(posts.get(0).getPostId(), actualPosts.get(0).getPostId());
  }

  @Test(expected = EntityNotFoundException.class)
  public void findPostsByUserId_JSONNotParsable_EntityNotFoundException() throws IOException {
    String postsJson = (new ObjectMapper()).writeValueAsString(posts);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(postsJson);
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenThrow(IOException.class);
    postSender.findPostsByUserId(1L);
  }

  @Test
  public void findPostsByPostIds_ListOfPosts_Success() throws IOException {
    List<Long> postIdList = new ArrayList<>();
    postIdList.add(1L);
    String postsJson = (new ObjectMapper()).writeValueAsString(posts);

    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(postsJson);
    when(mapper.writeValueAsString(any())).thenReturn("message");
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(posts);

    List<Post> actualPost = postSender.findPostsByPostIds(postIdList);
    assertEquals(1, actualPost.size());
    assertEquals(posts.get(0).getPostId(), actualPost.get(0).getPostId());
  }

  @Test
  public void findPostsByPostIds_ListOfPosts_ZeroInput() throws JsonProcessingException {
    List<Long> postIdList = new ArrayList<>();
    List<Post> actualPost = postSender.findPostsByPostIds(postIdList);
    assertEquals(0, actualPost.size());
  }

  @Test(expected = RuntimeException.class)
  public void findPostsByPostIds_JSON_EntityNotFoundException() throws IOException {
    List<Long> postIdList = new ArrayList<>();
    postIdList.add(1L);
    String postsJson = (new ObjectMapper()).writeValueAsString(posts);

    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(postsJson);
    when(mapper.writeValueAsString(any())).thenReturn("message");
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenThrow(IOException.class);
    postSender.findPostsByPostIds(postIdList);
  }

  @Test
  public void findUserIdsByPostIds_HashMap_Success() throws IOException {
    List<Long> postIdList = new ArrayList<>();
    Map<Long, Long> postIdToUserId = new HashMap<Long, Long>();
    postIdToUserId.put(1L, 1L);
    String mapJson = (new ObjectMapper()).writeValueAsString(postIdToUserId);

    when(mapper.writeValueAsString(any())).thenReturn("message");
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(postIdToUserId);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);

    Map<Long, Long> actualPostIdToUserId = postSender.findUserIdsByPostIds(postIdList);
    assertEquals(postIdToUserId.get(1L), actualPostIdToUserId.get(1L));
  }

  @Test(expected = RuntimeException.class)
  public void findUserIdsByPostIds_JSON_RuntimeException() throws IOException {
    List<Long> postIdList = new ArrayList<>();
    Map<Long, Long> postIdToUserId = new HashMap<Long, Long>();
    postIdToUserId.put(1L, 1L);
    String mapJson = (new ObjectMapper()).writeValueAsString(postIdToUserId);

    when(mapper.writeValueAsString(any())).thenReturn("message");
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenThrow(IOException.class);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);

    postSender.findUserIdsByPostIds(postIdList);
  }
}
