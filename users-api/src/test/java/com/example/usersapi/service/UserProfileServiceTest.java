package com.example.usersapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserProfile;
import com.example.usersapi.model.wrapper.UserProfileWithUser;
import com.example.usersapi.repository.ProfileRepository;
import com.example.usersapi.repository.UserProfileRepository;
import com.example.usersapi.repository.UserRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserProfileServiceTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private UserProfileServiceImpl userProfileService;

  @InjectMocks
  private User user;

  @InjectMocks
  private UserProfile userProfile;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ProfileRepository profileRepository;

  @Mock
  private UserProfileRepository userProfileRepository;

  private UserProfileWithUser userProfileWithUser;

  @Before
  public void init() {
    user.setId(1L);
    user.setEmail("email1@email.com");
    user.setUsername("user1");
    user.setPassword("pwd1");

    userProfile.setId(1L);
    userProfile.setAdditionalEmail("email2@email.com");
    userProfile.setMobile("11111");
    userProfile.setAddress("nyc");

    userProfileWithUser = new UserProfileWithUser(userProfile, user);
  }

  @Test
  public void createProfile_UserProfileWithUser_Success() {
    when(profileRepository.save(any())).thenReturn(userProfile);
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(userProfileRepository.update(anyLong(), anyLong())).thenReturn(1);
    UserProfileWithUser userProfileWithUser = userProfileService.createProfile("xx", userProfile);
    assertEquals(userProfile.getId(), userProfileWithUser.getId());
    assertEquals(user.getId(), userProfileWithUser.getUser().getId());
  }

  @Test(expected = RuntimeException.class)
  public void createProfile_() {
    when(profileRepository.save(any())).thenReturn(null);
    userProfileService.createProfile("xx", userProfile);
  }

  @Test
  public void updateProfile_UserProfileWithUser_Success() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(userProfileRepository.findProfileIdByUserId(anyLong())).thenReturn(1L);
    when(profileRepository.findById(anyLong())).thenReturn(Optional.of(userProfile));
    when(profileRepository.save(any())).thenReturn(userProfile);

    UserProfileWithUser userProfileWithUser = userProfileService.updateProfile("xx", userProfile);
    assertEquals(userProfile.getId(), userProfileWithUser.getId());
    assertEquals(user.getId(), userProfileWithUser.getUser().getId());
  }

  @Test
  public void updateProfile_ProfileNotFound_CreateProfile() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(userProfileRepository.findProfileIdByUserId(anyLong()))
        .thenThrow(EmptyResultDataAccessException.class);
    when(profileRepository.save(any())).thenReturn(userProfile);
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(userProfileRepository.update(anyLong(), anyLong())).thenReturn(1);

    UserProfileWithUser userProfileWithUser = userProfileService.updateProfile("xx", userProfile);
    assertEquals(userProfile.getId(), userProfileWithUser.getId());
    assertEquals(user.getId(), userProfileWithUser.getUser().getId());
  }

  @Test
  public void updateProfile_NullInput_Success() {
    UserProfile newProfile = new UserProfile();
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(userProfileRepository.findProfileIdByUserId(anyLong())).thenReturn(1L);
    when(profileRepository.findById(anyLong())).thenReturn(Optional.of(userProfile));
    when(profileRepository.save(any())).thenReturn(userProfile);

    UserProfileWithUser userProfileWithUser = userProfileService.updateProfile("xx", userProfile);
    assertEquals(userProfile.getId(), userProfileWithUser.getId());
    assertEquals(user.getId(), userProfileWithUser.getUser().getId());
  }

  @Test
  public void getProfile_UserProfileWithUser_Success() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(userProfileRepository.findProfileIdByUserId(anyLong())).thenReturn(1L);
    when(profileRepository.findById(anyLong())).thenReturn(Optional.of(userProfile));
    UserProfileWithUser actualProfileWithUser = userProfileService.getProfile("xx");

    assertEquals(userProfile.getId(), actualProfileWithUser.getId());
    assertEquals(user.getId(), actualProfileWithUser.getUser().getId());
  }
}
