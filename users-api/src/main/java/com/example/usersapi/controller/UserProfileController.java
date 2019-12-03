package com.example.usersapi.controller;

import com.example.usersapi.model.UserProfile;
import com.example.usersapi.model.wrapper.UserProfileWithUser;
import com.example.usersapi.service.UserProfileService;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
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
  @ApiOperation(value = "Create a profile of a user ", notes = "create profile", response = UserProfileWithUser.class)
  public UserProfileWithUser createUserProfile(@RequestHeader String username,
      @Valid @RequestBody UserProfile userProfile) {
    return userProfileService.createProfile(username, userProfile);
  }

  @PutMapping("/profile")
  @ApiOperation(value = "Update a profile of a user ", notes = "update profile", response = UserProfileWithUser.class)
  public UserProfileWithUser updateUserProfile(@RequestHeader String username,
      @RequestBody UserProfile userProfile) {
    return userProfileService.updateProfile(username, userProfile);
  }

  @GetMapping("/profile")
  @ApiOperation(value = "Get a profile of a user ", notes = "get profile", response = UserProfileWithUser.class)
  public UserProfileWithUser getUserProfile(@RequestHeader String username) {
    return userProfileService.getProfile(username);
  }
}
