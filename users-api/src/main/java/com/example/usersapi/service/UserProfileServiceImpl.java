package com.example.usersapi.service;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserProfile;
import com.example.usersapi.model.wrapper.UserProfileWithUser;
import com.example.usersapi.repository.ProfileRepository;
import com.example.usersapi.repository.UserProfileRepository;
import com.example.usersapi.repository.UserRepository;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProfileRepository profileRepository;

  @Autowired
  private UserProfileRepository userProfileRepository;

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public UserProfileWithUser createProfile(String username, @Valid UserProfile userProfile) {
    UserProfile savedProfile = profileRepository.save(userProfile);
    User user = userRepository.findByUsername(username);
    if (savedProfile != null && user != null
        && userProfileRepository.update(user.getId(), savedProfile.getId()) == 1) {
      return new UserProfileWithUser(savedProfile, user);
    }
    throw new RuntimeException("failed to save user profile");
  }

  @Override
  public UserProfileWithUser updateProfile(String username, UserProfile userProfile) {
    User user = userRepository.findByUsername(username);
    Long userId = user.getId();
    Long userProfileId = 0L;
    try {
      userProfileId = userProfileRepository.findProfileIdByUserId(userId);
    } catch (EmptyResultDataAccessException e) {
      logger.warn("this user " + username + " has no profile yet");
      return this.createProfile(username, userProfile);
    }
    UserProfile oldUserProfile = profileRepository.findById(userProfileId).orElse(null);
    if (userProfile.getAddress() != null) {
      oldUserProfile.setAddress(userProfile.getAddress());
    }
    if (userProfile.getMobile() != null) {
      oldUserProfile.setMobile(userProfile.getMobile());
    }
    if (userProfile.getAdditionalEmail() != null) {
      oldUserProfile.setAdditionalEmail(userProfile.getAdditionalEmail());
    }
    UserProfile updatedUserProfile = profileRepository.save(oldUserProfile);
    return new UserProfileWithUser(updatedUserProfile, user);
  }

  @Override
  public UserProfileWithUser getProfile(String username) {
    User user = userRepository.findByUsername(username);
    Long userId = user.getId();
    Long userProfileId = userProfileRepository.findProfileIdByUserId(userId);
    UserProfile oldUserProfile = profileRepository.findById(userProfileId).orElse(null);
    return new UserProfileWithUser(oldUserProfile, user);
  }
}
