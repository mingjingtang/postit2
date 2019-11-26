package com.example.usersapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.wrapper.RoleWithUsers;
import com.example.usersapi.model.wrapper.UserWithRoles;
import com.example.usersapi.service.UserRoleService;
import java.util.ArrayList;
import java.util.List;
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
@SpringBootTest(classes = {UserRoleController.class})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class,
    DataSourceAutoConfiguration.class})
public class UserRoleControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @MockBean
  private UserRoleService userRoleService;

  @InjectMocks
  private User user;

  @InjectMocks
  private UserRole userRole;

  private UserWithRoles userWithRoles;

  private RoleWithUsers roleWithUsers;

  List<User> users;

  List<UserRole> roles;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    user.setId(1L);
    user.setEmail("email1@email.com");
    user.setUsername("user1");
    user.setPassword("pwd1");

    userRole.setId(1L);
    userRole.setName("ROLE_USER");

    users = new ArrayList<>();
    users.add(user);
    roles = new ArrayList<>();
    roles.add(userRole);

    userWithRoles = new UserWithRoles(user, roles);
    roleWithUsers = new RoleWithUsers(userRole, users);
  }

  @Test
  public void getRole_userRole_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/role").header("username", "user1");

    when(userRoleService.getRoleByUsername(anyString())).thenReturn(userWithRoles);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"username\":\"user1\",\"roles\":[{\"id\":1,\"name\":\"ROLE_USER\"}]}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void createRole_listOfRoles_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/role").header("username", "user1")
        .contentType(MediaType.APPLICATION_JSON).content("{" + "\"name\":\"ROLE_USER2\"" + "}");

    when(userRoleService.createRole(any())).thenReturn(roles);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
        .andExpect(content().json("[{\"id\":1,\"name\":\"ROLE_USER\"}]")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void deleteRole_ListOfRoles_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/role/ROLE_USER")
        .header("username", "user1");

    when(userRoleService.deleteRole(anyString())).thenReturn(roles);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
        .andExpect(content().json("[{\"id\":1,\"name\":\"ROLE_USER\"}]")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void listAllRoles_ListOfRoles_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/roles")
        .header("username", "user1");

    when(userRoleService.listAllRoles()).thenReturn(roles);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
        .andExpect(content().json("[{\"id\":1,\"name\":\"ROLE_USER\"}]")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void findUsersByRole_RoleWithUsers_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/role/ROLE_USER")
        .header("username", "user1");

    when(userRoleService.findUsersByRole(anyString())).thenReturn(roleWithUsers);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"name\":\"ROLE_USER\",\"users\":[{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}]}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void assginRoleToUser_UserWithRoles_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/role/1/ROLE_USER")
        .header("username", "user1");

    when(userRoleService.assginRoleToUser(anyString(), anyLong())).thenReturn(userWithRoles);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"username\":\"user1\",\"roles\":[{\"id\":1,\"name\":\"ROLE_USER\"}]}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void removeRoleFromUser_UserWithRoles_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/role/1/ROLE_USER")
        .header("username", "user1");

    when(userRoleService.removeRoleFromUser(anyString(), anyLong())).thenReturn(userWithRoles);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"username\":\"user1\",\"roles\":[{\"id\":1,\"name\":\"ROLE_USER\"}]}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

}
