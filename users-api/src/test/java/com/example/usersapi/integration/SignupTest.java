package com.example.usersapi.integration;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.usersapi.model.User;
import com.example.usersapi.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
@DirtiesContext
public class SignupTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @MockBean
  private AmqpTemplate amqpTemplate;

  @Before
  public void init(){}

  @After
  public void clear(){
    User user = userRepository.findByUsername("user1");
    if(userRepository.findByUsername("user1") != null) {
      userRepository.delete(user);
    }
  }

  @Test
  public void Signup_User_Success() throws Exception {

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{" + "\"email\":\"email1@email.com\"," + "\"username\":\"user1\","
                + "\"password\":\"pwd1\"" + "}");

    MvcResult token = mockMvc.perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn();

    assertNotNull(token);
    System.out.println(token.getResponse().getContentAsString());
  }

  @Test
  public void Signup_Validator_Email() throws Exception {

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{" + "\"email\":\"email1email.com\"," + "\"username\":\"user1\","
            + "\"password\":\"pwd1\"" + "}");

    MvcResult result = mockMvc.perform(requestBuilder)
        .andExpect(status().is4xxClientError())
        .andReturn();

    System.out.println(result.getResolvedException().getMessage());
  }

  @Test
  public void Signup_Duplicate_Username() throws Exception {

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{" + "\"email\":\"email1@email.com\"," + "\"username\":\"user1\","
            + "\"password\":\"pwd1\"" + "}");

    MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    result = mockMvc.perform(requestBuilder).andExpect(status().is5xxServerError()).andReturn();

    System.out.println(result.getResolvedException().getMessage());
  }
}
