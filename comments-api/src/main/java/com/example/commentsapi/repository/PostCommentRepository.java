package com.example.commentsapi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostCommentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Long postId, Long commentId) {
        return jdbcTemplate.update("INSERT INTO post_comment(post_id, comment_id) "
                + "VALUES(?,?)", postId, commentId);
    }

    public int delete(Long commentId) {
        return jdbcTemplate.update("DELETE FROM post_comment WHERE comment_id = ?", commentId);
    }
}
