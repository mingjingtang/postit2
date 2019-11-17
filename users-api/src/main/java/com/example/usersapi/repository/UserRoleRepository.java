package com.example.usersapi.repository;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    public UserRole findByName(String defaultRoleName);
}
