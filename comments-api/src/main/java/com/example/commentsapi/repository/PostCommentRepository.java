package com.example.commentsapi.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.commentsapi.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostCommentRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private CommentRepository commentRepository;

  public int save(Long postId, Long commentId) {
    return jdbcTemplate
        .update("INSERT INTO post_comment(post_id, comment_id) " + "VALUES(?,?)", postId,
            commentId);
  }

  public int delete(Long commentId) {
    return jdbcTemplate.update("DELETE FROM post_comment WHERE comment_id = ?", commentId);
  }

  public Map<Long, Long> findPostIdsByCommentIds(List<Long> commentIdList) {
    if (commentIdList.size() == 0) {
      return new HashMap<Long, Long>();
    }
    String sql = "select * from post_comment where comment_id in(:commentids)";
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("commentids", commentIdList);
    List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sql, queryParams);
    Map<Long, Long> commentIdToPostId = results.stream().collect(Collectors
        .toMap(k -> new Long((Integer) k.get("comment_id")),
            k -> new Long((Integer) k.get("post_id"))));
    return commentIdToPostId;
  }

  public List<Comment> findCommentsByPostId(Long postId) {
    String sql = "select comment_id from post_comment where post_id=" + postId;
    List<Long> commentIdList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("comment_id"));

    List<Comment> commentList = new ArrayList<>();
    commentIdList
        .forEach(commentId -> commentList.add(commentRepository.findById(commentId).orElse(null)));
    return commentList;
  }

  public Map<Long, Long> findUserIdsByCommentIds(List<Long> commentIdList) {
    String sql = "select * from user_comment comm where comment_id in(:commentids)";
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("commentids", commentIdList);
    List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sql, queryParams);
    Map<Long, Long> commentIdToUserId = results.stream().collect(Collectors
        .toMap(k -> new Long((Integer) k.get("comment_id")),
            k -> new Long((Integer) k.get("user_id"))));
    return commentIdToUserId;
  }
}
