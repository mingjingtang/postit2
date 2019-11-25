package com.example.usersapi.repository;

import com.example.usersapi.model.User;
import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("FROM User u WHERE u.email = ?1 and u.password = ?2")
  public User login(String email, String password);

  public User findByUsername(String username);

  public User findByEmail(String email);

  @Query("FROM User u WHERE u.id IN (?1)")
  public List<User> findUsersByIds(List<Long> userIdList);

}