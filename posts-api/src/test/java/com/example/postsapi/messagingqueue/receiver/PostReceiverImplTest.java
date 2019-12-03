package com.example.postsapi.messagingqueue.receiver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.repository.PostUserRepository;
import com.example.postsapi.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class PostReceiverImplTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private PostReceiverImpl postReceiver;

  @Mock
  private PostService postService;

  @Mock
  private PostUserRepository postUserRepository;

  @InjectMocks
  private Post post;

  @InjectMocks
  private User user;

  private PostWithUser postWithUser;

  private List<Post> posts;

  private ObjectMapper mapper = new ObjectMapper();

  @Before
  public void init() {
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("desp");

    user.setId(1L);
    user.setUsername("user1");
    user.setEmail("email1");

    postWithUser = new PostWithUser(post, user);
    posts = new ArrayList<>();
    posts.add(post);
  }

  @Test
  public void handleMessage_findByPostId_PostInJson_Success() throws JsonProcessingException {
    Long postId = 1L;
    String msg = "findByPostId:" + postId;
    when(postService.findByPostId(anyLong())).thenReturn(postWithUser);
    String postJson = mapper.writeValueAsString(postWithUser);
    String actualPostJson = postReceiver.handleMessage_findByPostId(msg);
    assertEquals(postJson, actualPostJson);
  }

  @Test
  public void handleMessage_findByPostId_PostInJson_Failure() throws JsonProcessingException {
    Long postId = 1L;
    String msg = "wrong:" + postId;

    String actualPostJson = postReceiver.handleMessage_findByPostId(msg);

    assertEquals("", actualPostJson);
  }

  @Test
  public void handleMessage_findPostsByUserId_PostsInJson_Success() throws JsonProcessingException {
    Long userId = 1L;
    String msg = "findPostsByUserId:" + userId;
    when(postService.findPostsByUserId(anyLong())).thenReturn(posts);
    String postsJson = mapper.writeValueAsString(posts);
    String actualPostsJson = postReceiver.handleMessage_findPostsByUserId(msg);
    assertEquals(postsJson, actualPostsJson);
  }

  @Test
  public void handleMessage_findPostsByUserId_PostsInJson_Failure() throws JsonProcessingException {
    Long userId = 1L;
    String msg = "wrong:" + userId;

    String actualPostJson = postReceiver.handleMessage_findPostsByUserId(msg);

    assertEquals("", actualPostJson);
  }

  @Test
  public void handleMessage_findPostsByPostIds_PostsInJson_Success() throws IOException {
    List<Long> postIds = new ArrayList<>();
    postIds.add(1L);
    String msg = mapper.writeValueAsString(postIds);

    when(postService.findPostsByPostIds(anyList())).thenReturn(posts);

    String postsJson = mapper.writeValueAsString(posts);
    String actualPostsJson = postReceiver.handleMessage_findPostsByPostIds(msg);
    assertEquals(postsJson, actualPostsJson);
  }

  @Test
  public void handleMessage_findUserIdsByPostIds_MapPostIdToUserId_Success() throws IOException {
    List<Long> postIds = new ArrayList<>();
    postIds.add(1L);
    String msg = mapper.writeValueAsString(postIds);
    Map<Long, Long> postIdToUserId = new HashMap<>();
    postIdToUserId.put(1L, 2L);

    when(postUserRepository.findUserIdsByCommentIds(anyList())).thenReturn(postIdToUserId);

    String mapJson = mapper.writeValueAsString(postIdToUserId);
    String actualMapJson = postReceiver.handleMessage_findUserIdsByPostIds(msg);
    assertEquals(mapJson, actualMapJson);
  }
}
