package com.example.usersapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.usersapi.config.JwtUtil;
import com.example.usersapi.model.Comment;
import com.example.usersapi.model.Post;
import com.example.usersapi.model.User;
import com.example.usersapi.model.wrapper.CommentWithDetails;
import com.example.usersapi.model.wrapper.PostWithUser;
import com.example.usersapi.repository.CommentRepository;
import com.example.usersapi.repository.PostRepository;
import com.example.usersapi.repository.ProfileRepository;
import com.example.usersapi.repository.RoleRepository;
import com.example.usersapi.repository.UserProfileRepository;
import com.example.usersapi.repository.UserRepository;
import com.example.usersapi.repository.UserRoleRepository;
import com.example.usersapi.service.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserController.class})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class,
    DataSourceAutoConfiguration.class})
public class UserControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private JwtUtil jwtUtil;

  @InjectMocks
  private User user;

  @InjectMocks
  private Post post;

  @InjectMocks
  private Comment comment;

  private List<User> userList;

  private List<PostWithUser> postWithUserList;

  private List<CommentWithDetails> commentWithDetailsList;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    user.setId(1L);
    user.setEmail("email1@email.com");
    user.setUsername("user1");
    user.setPassword("pwd1");
  }

  @Test
  public void signup_Returns200_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/signup")
        .contentType(MediaType.APPLICATION_JSON).content(
            "{" + "\"email\":\"email1@email.com\"," + "\"username\":\"user1\","
                + "\"password\":\"pwd1\"" + "}");

    when(userService.signup(any())).thenReturn("123456");
    when(userService.findByEmail(anyString())).thenReturn(user);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
        .andExpect(content().json("{\"token\":\"123456\", \"username\":\"user1\"}")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void login_Returns200_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{" + "\"email\":\"email1@email.com\"," + "\"password\":\"pwd1\"" + "}");

    when(userService.login(any())).thenReturn("123456");
    when(userService.findByEmail(anyString())).thenReturn(user);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
        .andExpect(content().json("{\"token\":\"123456\", \"username\":\"user1\"}")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void listUsers_UserList_Success() throws Exception {
    userList = new ArrayList<>();
    userList.add(user);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/list");

    when(userService.listUsers()).thenReturn(userList);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json("[{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}]"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void getPostsByUser_listOfPostsWithUsers_Success() throws Exception {
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("description");
    PostWithUser postWithUser = new PostWithUser(post, user);
    postWithUserList = new ArrayList<>();
    postWithUserList.add(postWithUser);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/post").header("username", "user1");

    when(userService.getPostsByUser(anyString())).thenReturn(postWithUserList);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "[{\"postId\":1,\"title\":\"title\",\"description\":\"description\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}}]"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void getCommentsByUser_CommentWithDetailsList_Success() throws Exception {
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("description");
    comment.setCommentId(1L);
    comment.setText("comment");
    CommentWithDetails commentWithDetails = new CommentWithDetails(comment, user, post, user);
    commentWithDetailsList = new ArrayList<>();
    commentWithDetailsList.add(commentWithDetails);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/comment")
        .header("username", "user1");

    when(userService.getCommentsByUser(anyString())).thenReturn(commentWithDetailsList);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "[{\"id\":1,\"text\":\"comment\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"},\"post\":{\"postId\":1,\"title\":\"title\",\"description\":\"description\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}}}]"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }
}
