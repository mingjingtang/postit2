package com.example.usersapi.controller;

import com.example.usersapi.model.*;
import com.example.usersapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    String token = userService.login(user);
    User foundUser = userService.findByEmail(user.getEmail());
    return ResponseEntity.ok(new JwtResponse(token, foundUser.getUsername()));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody User user) {
    String token = userService.signup(user);
    User savedUser = userService.findByEmail(user.getEmail());
    return ResponseEntity.ok(new JwtResponse(token, savedUser.getUsername()));
  }

  @GetMapping("/list")
  public List<User> listUsers() {

    return userService.listUsers();
  }


  @GetMapping("/post")
  public List<PostWithUser> getPostsByUser(@RequestHeader String username) {
    return userService.getPostsByUser(username);
  }

  @GetMapping("/comment")
  public List<CommentWithDetails> getCommentsByUser(@RequestHeader String username)
      throws JsonProcessingException {
    return userService.getCommentsByUser(username);
  }

  @PostMapping("/profile")
  public UserProfile postUserProfile(@RequestHeader String username, @RequestBody UserProfile userProfile) {
      return userService.createProfile(username, userProfile);
  }

  @PutMapping("/profile")
  public UserProfile updateUserProfile(@RequestHeader String username, @RequestBody UserProfile userProfile){
      return userService.updateProfile(username, userProfile);
  }

  @GetMapping("/profile")
  public UserProfile getUserProfile(@RequestHeader String username){
      return userService.getProfile(username);
  }
}
