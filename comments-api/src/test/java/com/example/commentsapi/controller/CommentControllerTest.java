package com.example.commentsapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.model.CommentWithDetails;
import com.example.commentsapi.model.Post;
import com.example.commentsapi.model.PostWithUser;
import com.example.commentsapi.model.User;
import com.example.commentsapi.service.CommentService;
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
@SpringBootTest(classes = {CommentController.class})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class,
    DataSourceAutoConfiguration.class})

public class CommentControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @InjectMocks
  private User user;

  @InjectMocks
  private Post post;

  @InjectMocks
  private Comment comment;

  private PostWithUser postWithUser;
  private CommentWithDetails commentWithDetails;

  @MockBean
  private CommentService commentService;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    user.setId(1L);
    user.setEmail("email1@email.com");
    user.setUsername("user1");
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("description");
    postWithUser = new PostWithUser(post, user);
    comment.setCommentId(1L);
    comment.setText("comment");
    commentWithDetails = new CommentWithDetails();
    commentWithDetails.setId(comment.getCommentId());
    commentWithDetails.setText(comment.getText());
    commentWithDetails.setPost(postWithUser);
    commentWithDetails.setUser(user);
  }

  @Test
  public void createComment_CommentCreated_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/1").header("username", "username")
        .contentType(MediaType.APPLICATION_JSON).content("{" + "\"text\" : \"comment\"" + "}");

    when(commentService.createComment(anyString(), anyLong(), any()))
        .thenReturn(commentWithDetails);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(
        content().json(
            "{\"id\":1,\"text\":\"comment\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"},\"post\":{\"postId\":1,\"title\":\"title\",\"description\":\"description\",\"user\":{\"id\":1,\"username\":\"user1\",\"email\":\"email1@email.com\"}}}"))
        .andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }

  @Test
  public void deleteComment_Success() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/1")
        .header("username", "username");

    when(commentService.deleteComment(anyString(), anyLong())).thenReturn(1L);

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk())
        .andExpect(content().string("1")).andReturn();
    System.out.println(result.getResponse().getContentAsString());
  }
}
