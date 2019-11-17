package com.example.usersapi.service;

import com.example.usersapi.model.UserProfile;
import com.example.usersapi.model.wrapper.UserProfileWithUser;

public interface UserProfileService {
  public UserProfileWithUser createProfile(String username, UserProfile profile);

  public UserProfileWithUser updateProfile(String username, UserProfile userProfile);

  public UserProfileWithUser getProfile(String username);
}
