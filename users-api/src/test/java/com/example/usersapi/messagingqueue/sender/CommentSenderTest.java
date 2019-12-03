package com.example.usersapi.messagingqueue.sender;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.usersapi.model.Comment;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.amqp.core.AmqpTemplate;

public class CommentSenderTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private CommentSenderImpl commentSender;

  @InjectMocks
  private Comment comment;

  @Mock
  private AmqpTemplate amqpTemplate;

  @Mock
  private ObjectMapper mapper = new ObjectMapper();

  private List<Comment> comments;

  @Before
  public void init() {
    comment.setCommentId(1L);
    comment.setText("comment1");

    comments = new ArrayList<>();
    comments.add(comment);
  }

  @Test
  public void findCommentsByUserId_ListOfComments_Success() throws IOException {
    String mapJson = (new ObjectMapper()).writeValueAsString(comments);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(comments);
    List<Comment> actualCommentList = commentSender.findCommentsByUserId(1L);
    assertEquals(comments.size(), actualCommentList.size());
    assertEquals(comments.get(0).getCommentId(), actualCommentList.get(0).getCommentId());
  }

  @Test(expected = RuntimeException.class)
  public void findCommentsByUserId_RuntimeException() throws IOException {
    String mapJson = (new ObjectMapper()).writeValueAsString(comments);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenThrow(IOException.class);
    commentSender.findCommentsByUserId(1L);
  }

  @Test
  public void findPostIdsByCommentIds_MapCommentIdToPostId_Success() throws IOException {
    Map<Long, Long> commentIdToPostId = new HashMap<>();
    commentIdToPostId.put(1L, 1L);
    List<Long> commentIds = new ArrayList<>(commentIdToPostId.keySet());
    String mapJson = (new ObjectMapper()).writeValueAsString(commentIdToPostId);

    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(commentIdToPostId);
    when(mapper.writeValueAsString(any())).thenReturn("message");

    Map<Long, Long> actualCommentIdToPostId = commentSender.findPostIdsByCommentIds(commentIds);
    assertEquals(commentIdToPostId.get(1L), actualCommentIdToPostId.get(1L));
  }

  @Test
  public void findPostIdsByCommentIds_ZeroInput() throws IOException {
    Map<Long, Long> commentIdToPostId = new HashMap<>();
    commentIdToPostId.put(1L, 1L);
    List<Long> commentIds = new ArrayList<>();

    Map<Long, Long> actualCommentIdToPostId = commentSender.findPostIdsByCommentIds(commentIds);
    assertEquals(0, actualCommentIdToPostId.size());
  }

  @Test(expected = RuntimeException.class)
  public void findPostIdsByCommentIds_RuntimeException() throws IOException {
    Map<Long, Long> commentIdToPostId = new HashMap<>();
    commentIdToPostId.put(1L, 1L);
    List<Long> commentIds = new ArrayList<>(commentIdToPostId.keySet());
    String mapJson = (new ObjectMapper()).writeValueAsString(commentIdToPostId);

    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);
    when(mapper.readValue(anyString(), any(TypeReference.class))).thenThrow(IOException.class);
    when(mapper.writeValueAsString(any())).thenReturn("message");

    commentSender.findPostIdsByCommentIds(commentIds);
  }
}
