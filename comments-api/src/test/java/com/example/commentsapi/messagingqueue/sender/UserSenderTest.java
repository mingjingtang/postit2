package com.example.commentsapi.messagingqueue.sender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.commentsapi.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.amqp.core.AmqpTemplate;

public class UserSenderTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private UserSenderImpl userSender;

  @Mock
  private AmqpTemplate amqpTemplate;

  @InjectMocks
  private User user;

  private ObjectMapper mapper = new ObjectMapper();

  @Before
  public void init() {
    user.setId(1L);
    user.setEmail("email1");
    user.setUsername("user1");
  }

  @Test
  public void findByUsername_User_Success() throws JsonProcessingException {
    String userJson = mapper.writeValueAsString(user);
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(userJson);
    User actualUser = userSender.findByUsername("user1");
    assertNotNull(actualUser);
    assertEquals(user.getEmail(), actualUser.getEmail());
    assertEquals(user.getUsername(), actualUser.getUsername());
  }

  @Test(expected = EntityNotFoundException.class)
  public void findByUsername_UserNotFound_EntityNotFoundException() throws JsonProcessingException {
    when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn("");
    userSender.findByUsername("user1");
  }
}
