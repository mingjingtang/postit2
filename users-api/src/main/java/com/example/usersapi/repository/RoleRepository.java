package com.example.usersapi.repository;

import com.example.usersapi.model.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<UserRole, Long> {

  public UserRole findByName(String defaultRoleName);
}