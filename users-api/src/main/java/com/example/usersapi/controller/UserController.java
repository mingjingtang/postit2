package com.example.usersapi.controller;

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

  @PostMapping("/signup")
  public User signup(@RequestBody User user) {

    return userService.signup(user);
  }

  @PostMapping("/login")
  public User login(@RequestBody User user) {

    return userService.login(user);
  }

  @GetMapping("/list")
  public List<User> listUsers() {

    return userService.listUsers();
  }

//  @PutMapping("/{userId}")
//  public User updateUser(@RequestBody User user, @PathVariable Long userId){
//      return userService.updateUser(user, userId);
//  }

//  @GetMapping("/post")
//  public List<Post> getPostsByUser() {
//
//    String username = securityUtils.getAuthenticatedUsername();
//    return userService.getPostsByUser(username);
//  }
//
//  @GetMapping("/comment")
//  public List<Comment> getCommentsByUser() {
//
//    String username = securityUtils.getAuthenticatedUsername();
//    return userService.getCommentsByUser(username);
//  }
}
