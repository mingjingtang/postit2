package com.example.usersapi.repository;

import com.example.usersapi.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserUserRoleRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int update(Long userId, int userRoleId) {
        return jdbcTemplate
                .update("insert into user_role (user_id, role_id) " + "values(?,?)", userId, userRoleId);
    }
}
