package com.example.usersapi.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.repository.RoleRepository;
import com.example.usersapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
@DirtiesContext
public class UserIntegrationTest {

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @MockBean
  private AmqpTemplate amqpTemplate;

  private UserRole createUserRole(){
    UserRole userRole = roleRepository.findByName("ROLE_ADMIN");
    if (userRole == null) {
      userRole = new UserRole();
      userRole.setName("ROLE_ADMIN");
      userRole = roleRepository.save(userRole);
    }
    return userRole;
  }

  private User createUser() {
    UserRole userRole = createUserRole();

    User user = new User();
    user.setUsername("batman");
    user.setEmail("batman@email.com");
    user.setPassword("bat");

    return user;
  }

  @Test
  public void signup_User_Success() {
    User user = userRepository.findByUsername("batman");
    if (user != null) {
      userRepository.delete(user);
    }
    user = createUser();
    user = userRepository.save(user);
    User foundUser = userRepository.findByUsername(user.getUsername());

    assertNotNull(user);
    assertNotNull(foundUser);
    assertEquals(user.getId(), foundUser.getId());

    userRepository.delete(user);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void signup_DuplicateUsername_Exception() {
    User user = createUser();
    userRepository.save(user);
    user.setId(null);
    userRepository.save(user);
  }

  @Test
  public void login_User_Success() {
    signup_User_Success();

    User user = createUser();
    user = userRepository.save(user);
    User foundUser = userRepository.findByEmail(user.getEmail());
    System.out.println(foundUser);

    assertNotNull(user);
    assertNotNull(foundUser);
    assertEquals(user.getId(), foundUser.getId());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void login_EmailNull_Exception() {
    User user = createUser();
    userRepository.save(user);
    user.setEmail(null);
    userRepository.save(user);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void login_PasswordNull_Exception() {
    User user = createUser();
    userRepository.save(user);
    user.setPassword(null);
    userRepository.save(user);
  }
}

