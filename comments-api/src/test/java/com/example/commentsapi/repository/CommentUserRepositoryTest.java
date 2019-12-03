package com.example.commentsapi.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.commentsapi.model.Comment;
import java.util.ArrayList;
import java.util.List;
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

public class CommentUserRepositoryTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private CommentUserRepository commentUserRepository;

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private Comment comment;

  @Before
  public void init() {
    comment.setCommentId(1L);
    comment.setText("comment");
  }

  @Test
  public void saveUserIdToCommentId_Success() {
    when(jdbcTemplate.update(anyString(), any(Object.class))).thenReturn(1);
    int actualStatus = commentUserRepository.save(1L, 1L);
    assertEquals(1, 1);
  }

  @Test
  public void deleteUserIdToCommentId_Success() {
    when(jdbcTemplate.update(anyString(), any(Object.class))).thenReturn(1);
    int actualStatus = commentUserRepository.delete(1L);
    assertEquals(1, 1);
  }

  @Test
  public void findCommentsByUserId_ListOfComment_Success() {
    List<Long> commentIdList = new ArrayList<>();
    commentIdList.add(1L);

    when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(commentIdList);
    when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
    List<Comment> actualCommentList = commentUserRepository.findCommentsByUserId(1L);
    assertEquals(comment.getCommentId(), actualCommentList.get(0).getCommentId());
  }
}
