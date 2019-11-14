package com.example.usersapi.controller;

import com.example.usersapi.model.JwtResponse;
import com.example.usersapi.model.PostWithDetails;
import com.example.usersapi.model.User;
import com.example.usersapi.service.UserService;
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
  public List<PostWithDetails> getPostsByUser(@RequestHeader String username) {
    return userService.getPostsByUser(username);
  }
//
//  @GetMapping("/comment")
//  public List<Comment> getCommentsByUser() {
//
//    String username = securityUtils.getAuthenticatedUsername();
//    return userService.getCommentsByUser(username);
//  }
}
