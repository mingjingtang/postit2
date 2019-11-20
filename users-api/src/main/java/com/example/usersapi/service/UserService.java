package com.example.usersapi.service;

import com.example.usersapi.model.wrapper.CommentWithDetails;
import com.example.usersapi.model.wrapper.PostWithUser;
import com.example.usersapi.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  public String signup(User user);

  public String login(User user);

  public List<User> listUsers();

  public Long findIdByUsername(String username);

  public User findByEmail(String email);

  public User findByUsername(String username);

  public User findById(Long userId);

  public List<PostWithUser> getPostsByUser(String username);

  public List<User> findUsersByIds(List<Long> userIdList);

  public List<CommentWithDetails> getCommentsByUser(String username) throws JsonProcessingException;

}
