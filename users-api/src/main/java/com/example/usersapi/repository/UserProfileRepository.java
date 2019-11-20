package com.example.usersapi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public int update(Long userId, Long profileId) {
    return jdbcTemplate
        .update("insert into user_profile (user_id, profile_id) " + "values(?,?)", userId,
            profileId);
  }

  public Long findProfileIdByUserId(Long userId) {
    String sql = "select profile_id from user_profile where user_id=" + userId;
    return jdbcTemplate.queryForObject(sql, Long.class);
  }
}
