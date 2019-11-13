package com.example.commentsapi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserCommentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Long userId, Long commentId) {
        return jdbcTemplate.update("INSERT INTO user_comment(user_id, comment_id) "
                + "VALUES(?,?)", userId, commentId);
    }

    public int delete(Long commentId) {
        return jdbcTemplate.update("DELETE FROM user_comment WHERE comment_id = ?", commentId);
    }
}
