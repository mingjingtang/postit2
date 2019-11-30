package com.example.postsapi.messagingqueue.sender;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.amqp.core.AmqpTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CommentSenderImplTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private CommentSenderImpl commentSender;

  @InjectMocks
  private Comment comment;

  @InjectMocks
  User user;

  @InjectMocks
  Post post;

  @Mock
  private AmqpTemplate amqpTemplate;

  private List<Comment> comments;

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

    comment.setCommentId(1L);
    comment.setText("comment1");

    comments = new ArrayList<>();
    comments.add(comment);

    commentWithDetails = new CommentWithDetails(comment, user, postWithUser);
  }

  @Test
  public void createComment_Comment_Success() throws IOException {
    String commentJson = (new ObjectMapper()).writeValueAsString(comment);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(commentJson);
    Comment actualComment = commentSender
        .createComment(user.getUsername(), post.getPostId(), comment);
    assertNotNull(actualComment);
    assertEquals(comment.getText(), actualComment.getText());
  }

  @Test
  public void getCommentByPostId_ListComments_Success() throws IOException {
    String commentListJson = (new ObjectMapper()).writeValueAsString(comments);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(commentListJson);
    List<Comment> actualCommentList = commentSender.getCommentsByPostId(1L);
    assertNotNull(actualCommentList);
    assertEquals(comments.get(0).getCommentId(), actualCommentList.get(0).getCommentId());
  }

  @Test
  public void findPostIdByCommentId_Map_Success() throws IOException {
    Map<Long, Long> commentIdToPostId = new HashMap<>();
    commentIdToPostId.put(1L, 1L);
    List<Long> commentIds = new ArrayList<>(commentIdToPostId.keySet());
    String mapJson = (new ObjectMapper()).writeValueAsString(commentIdToPostId);

    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);

    Map<Long, Long> actualCommentIdToPostId = commentSender.findPostIdsByCommentIds(commentIds);
    assertEquals(commentIdToPostId.get(1L), actualCommentIdToPostId.get(1L));
  }
}
