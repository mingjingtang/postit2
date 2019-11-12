package com.example.postsapi.repository;

public interface UserRepository {

  public Long findIdByUsername(String username);
}
