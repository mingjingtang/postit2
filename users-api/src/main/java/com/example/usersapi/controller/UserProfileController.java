package com.example.usersapi.controller;

import com.example.usersapi.model.UserProfile;
import com.example.usersapi.model.UserProfileWithUser;
import com.example.usersapi.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

  @Autowired
  private UserProfileService userProfileService;

  @PostMapping("/profile")
  public UserProfileWithUser postUserProfile(@RequestHeader String username, @RequestBody UserProfile userProfile) {
    return userProfileService.createProfile(username, userProfile);
  }

  @PutMapping("/profile")
  public UserProfileWithUser updateUserProfile(@RequestHeader String username, @RequestBody UserProfile userProfile){
    return userProfileService.updateProfile(username, userProfile);
  }

  @GetMapping("/profile")
  public UserProfileWithUser getUserProfile(@RequestHeader String username){
    return userProfileService.getProfile(username);
  }
}
