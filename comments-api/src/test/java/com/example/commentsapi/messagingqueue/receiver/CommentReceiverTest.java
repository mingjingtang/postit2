package com.example.commentsapi.messagingqueue.receiver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.repository.CommentPostRepository;
import com.example.commentsapi.repository.CommentUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

public class CommentReceiverTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private CommentReceiverImpl commentReceiver;

  @Mock
  private CommentUserRepository commentUserRepository;

  @Mock
  private CommentPostRepository commentPostRepository;

  @InjectMocks
  private Comment comment;

  private List<Comment> comments;

  private ObjectMapper mapper = new ObjectMapper();

  @Before
  public void init() {
    comment.setCommentId(1L);
    comment.setText("comment");

    comments = new ArrayList<>();
    comments.add(comment);
  }

  @Test
  public void handleMessage_findCommentsByUserId_Success() throws IOException {
    Long userId = 1L;
    String msg = "findCommentsByUserId:" + userId;

    when(commentUserRepository.findCommentsByUserId(anyLong())).thenReturn(comments);

    String returnedCommentsJson = commentReceiver.handleMessage_findCommentsByUserId(msg);
    List<Comment> actualComments = mapper
        .readValue(returnedCommentsJson, new TypeReference<List<Comment>>() {
        });
    assertEquals(1, actualComments.size());
    assertEquals(1l, (long) actualComments.get(0).getCommentId());
    assertEquals(comment.getText(), actualComments.get(0).getText());
  }

  @Test
  public void handleMessage_findPostIdsByCommentIds_Success() throws IOException {
    List<Long> commentIdList = new ArrayList<>();
    commentIdList.add(1L);
    String commentIdListJson = mapper.writeValueAsString(commentIdList);
    Map<Long, Long> commentIdToPostId = new HashMap<>();
    commentIdToPostId.put(1L, 2L);
    String commentIdToPostIdJson = mapper.writeValueAsString(commentIdToPostId);

    when(commentUserRepository.findCommentsByUserId(anyLong())).thenReturn(comments);
    when(commentPostRepository.findPostIdsByCommentIds(anyList())).thenReturn(commentIdToPostId);
    String returnedJson = commentReceiver.handleMessage_findPostIdsByCommentIds(commentIdListJson);
    Map<Long, Long> actualCommentIdToPostId = mapper
        .readValue(returnedJson, new TypeReference<HashMap<Long, Long>>() {
        });

    assertEquals(commentIdToPostIdJson, returnedJson);
    assertEquals(1, actualCommentIdToPostId.size());
    assertEquals(2l, (long) actualCommentIdToPostId.get(1L));
  }

  @Test
  public void handleMessage_getCommentsByPostId_Success() throws IOException {
    Long postId = 1L;
    String msg = "getCommentsByPostId:" + postId;

    when(commentPostRepository.findCommentsByPostId(anyLong())).thenReturn(comments);

    String returnedCommentsJson = commentReceiver.handleMessage_getCommentsByPostId(msg);
    List<Comment> actualComments = mapper
        .readValue(returnedCommentsJson, new TypeReference<List<Comment>>() {
        });
    assertEquals(1, actualComments.size());
    assertEquals(1l, (long) actualComments.get(0).getCommentId());
    assertEquals(comment.getText(), actualComments.get(0).getText());
  }

  @Test
  public void handleMessage_findCommentIdsToUserIds_Success() throws IOException {
    List<Long> commentIdList = new ArrayList<>();
    commentIdList.add(1L);
    String commentIdListJson = mapper.writeValueAsString(commentIdList);
    Map<Long, Long> commentIdToUserId = new HashMap<>();
    commentIdToUserId.put(1L, 2L);
    String commentIdToUserIdJson = mapper.writeValueAsString(commentIdToUserId);

    when(commentPostRepository.findUserIdsByCommentIds(anyList())).thenReturn(commentIdToUserId);
    String returnedJson = commentReceiver.handleMessage_findCommentIdsToUserIds(commentIdListJson);
    Map<Long, Long> actualCommentIdToUserId = mapper
        .readValue(returnedJson, new TypeReference<HashMap<Long, Long>>() {
        });

    assertEquals(commentIdToUserIdJson, returnedJson);
    assertEquals(1, actualCommentIdToUserId.size());
    assertEquals(2l, (long) actualCommentIdToUserId.get(1L));
  }

  @Test
  public void handleMessage_MsgIncorrect() throws IOException {
    String msg = "test";

    String returnedCommentsJson = "";
    returnedCommentsJson = commentReceiver.handleMessage_findCommentsByUserId(msg);
    assertEquals("", returnedCommentsJson);

    returnedCommentsJson = commentReceiver.handleMessage_getCommentsByPostId(msg);
    assertEquals("", returnedCommentsJson);
  }
}
