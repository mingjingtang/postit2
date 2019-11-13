package com.example.postsapi.repository;

import com.example.postsapi.model.User;

public interface UserRepository {

  public Long findIdByUsername(String username);

  public User findByUsername(String username);

  public  User findByUserId(Long userId);
}
