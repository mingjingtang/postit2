package com.example.postsapi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostUserRepository {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public int save(long userId, long postId) {
    return jdbcTemplate.update(
        "insert into post_user (user_id, post_id) " +
            "values(?,?)", userId, postId);
  }
}
