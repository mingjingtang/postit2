package com.example.usersapi.repository;

import com.example.usersapi.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("FROM User u WHERE u.email = ?1 and u.password = ?2")
  public User login(String email, String password);

  public User findByUsername(String username);

  public User findByEmail(String email);

  public User findUserById(Long userId);
}