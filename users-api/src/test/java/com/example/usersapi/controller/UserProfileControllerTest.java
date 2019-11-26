package com.example.usersapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserProfile;
import com.example.usersapi.model.wrapper.UserProfileWithUser;
import com.example.usersapi.service.UserProfileService;
import javax.ws.rs.core.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserProfileController.class})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class,
    DataSourceAutoConfiguration.class})
public class UserProfileControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @MockBean
  private UserProfileService userProfileService;

  @InjectMocks
  private UserProfile userProfile;

  @InjectMocks
  private User user;

  private UserProfileWithUser userProfileWithUser;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
  public void createUserProfile_ProfileCreated_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/profile")
        .header("username", "user1").contentType(MediaType.APPLICATION_JSON).content(
            "{" + "\"additionalEmail\":\"email1@email.com\"," + "\"mobile\":\"11111\","
                + "\"address\":\"nyc\"" + "}");
    ;

    when(userProfileService.createProfile(anyString(), any())).thenReturn(userProfileWithUser);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"mobile\":\"11111\",\"address\":\"nyc\",\"additionalEmail\":\"email2@email.com\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void updateUserProfile_ProfileUpdated_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/profile")
        .header("username", "user1").contentType(MediaType.APPLICATION_JSON).content(
            "{" + "\"additionalEmail\":\"email1@email.com\"," + "\"mobile\":\"11111\","
                + "\"address\":\"nyc\"" + "}");

    when(userProfileService.updateProfile(anyString(), any())).thenReturn(userProfileWithUser);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"mobile\":\"11111\",\"address\":\"nyc\",\"additionalEmail\":\"email2@email.com\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void getUserProfile_Profile_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/profile")
        .header("username", "user1");

    when(userProfileService.getProfile(anyString())).thenReturn(userProfileWithUser);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"mobile\":\"11111\",\"address\":\"nyc\",\"additionalEmail\":\"email2@email.com\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }
}
