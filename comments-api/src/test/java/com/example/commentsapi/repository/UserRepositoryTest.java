package com.example.commentsapi.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.commentsapi.messagingqueue.sender.UserSenderImpl;
import com.example.commentsapi.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class UserRepositoryTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private UserRepository userRepository;

  @Mock
  private UserSenderImpl userSender;

  @InjectMocks
  private User user;

  @Before
  public void init() {
    user.setId(1L);
    user.setEmail("email1");
    user.setUsername("user1");
  }

  @Test
  public void findByUsername_User_Success() {
    when(userSender.findByUsername(anyString())).thenReturn(user);
    User actualUser = userRepository.findByUsername("user1");
    assertEquals(user.getId(), actualUser.getId());
  }
}
