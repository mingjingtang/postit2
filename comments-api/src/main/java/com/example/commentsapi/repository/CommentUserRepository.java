package com.example.commentsapi.repository;

import com.example.commentsapi.model.Comment;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommentUserRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private CommentRepository commentRepository;

  public int save(Long userId, Long commentId) {
    return jdbcTemplate
        .update("INSERT INTO user_comment(user_id, comment_id) " + "VALUES(?,?)", userId,
            commentId);
  }

  public int delete(Long commentId) {
    return jdbcTemplate.update("DELETE FROM user_comment WHERE comment_id = ?", commentId);
  }

  public List<Comment> findCommentsByUserId(Long userId) {
    String sql = "select comment_id from user_comment where user_id=" + userId;
    List<Long> commentIdList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("comment_id"));

    List<Comment> commentList = new ArrayList<>();
    commentIdList
        .forEach(commentId -> commentList.add(commentRepository.findById(commentId).orElse(null)));
    return commentList;
  }
}
