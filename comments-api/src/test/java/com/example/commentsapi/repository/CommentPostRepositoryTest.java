package com.example.commentsapi.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.commentsapi.model.Comment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CommentPostRepositoryTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private CommentPostRepository commentPostRepository;

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private Comment comment;

  @Before
  public void init(){
    comment.setCommentId(1L);
    comment.setText("comment");
  }

  @Test
  public void savePostIdToCommentId_Success(){
    when(jdbcTemplate.update(anyString(), any(Object.class))).thenReturn(1);
    int actualStatus = commentPostRepository.save(1L, 1L);
    assertEquals(1, 1);
  }

  @Test
  public void deletePostIdToCommentId_Success(){
    when(jdbcTemplate.update(anyString(), any(Object.class))).thenReturn(1);
    int actualStatus = commentPostRepository.delete(1L);
    assertEquals(1, 1);
  }

  @Test
  public void findPostIdsByCommentIds_MapCommentIdToPostId_Success(){
    List<Long> commentIdList = new ArrayList<>();
    commentIdList.add(1L);
    List<Map<String, Object>> results = new ArrayList<>();
    Map<String, Object> resultSet = new HashMap<>();
    resultSet.put("comment_id", 1);
    resultSet.put("post_id", 2);
    results.add(resultSet);

    when(namedParameterJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(results);

    Map<Long, Long> commentIdToPostId = commentPostRepository.findPostIdsByCommentIds(commentIdList);
    assertEquals(1, commentIdToPostId.size());
    assertEquals(2l, (long)commentIdToPostId.get(1L));

  }

  @Test
  public void findPostIdsByCommentIds_MapCommentIdToPostId_ZeroInput(){
    List<Long> commentIdList = new ArrayList<>();
    Map<Long, Long> commentIdToPostId = commentPostRepository.findPostIdsByCommentIds(commentIdList);
    assertEquals(0, commentIdToPostId.size());
  }

  @Test
  public void findCommentsByPostId_ListOfComment_Success(){
    List<Long> commentIdList = new ArrayList<>();
    commentIdList.add(1L);

    when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(commentIdList);
    when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

    List<Comment> actualComments = commentPostRepository.findCommentsByPostId(1L);
    assertEquals(1, actualComments.size());
    assertEquals(comment.getCommentId(), actualComments.get(0).getCommentId());
  }

  @Test
  public void findUserIdsByCommentIds_MapCommentIdToUserId_Success(){
    List<Long> commentIdList = new ArrayList<>();
    List<Map<String, Object>> results = new ArrayList<>();
    Map<String, Object> resultSet = new HashMap<>();
    resultSet.put("comment_id", 1);
    resultSet.put("user_id", 2);
    results.add(resultSet);

    when(namedParameterJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(results);
    Map<Long, Long> actualCommentIdToUserId = commentPostRepository.findUserIdsByCommentIds(commentIdList);
    assertEquals(1, actualCommentIdToUserId.size());
    assertEquals(2l, (long)actualCommentIdToUserId.get(1L));
  }
}

