package com.example.postsapi.controller;

import com.example.postsapi.controller.PostController;
import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.example.postsapi.service.CommentService;
import com.example.postsapi.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostController.class})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class})

public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private PostService postService;


    @MockBean
    private CommentService commentService;

    @InjectMocks
    private Post post;

    @InjectMocks
    private Comment comment;

    @InjectMocks
    private User user;

    private PostWithUser postWithUser;

    private CommentWithDetails commentWithDetails;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();


        // dummy post
        post.setPostId(1L);
        post.setTitle("title");
        post.setDescription("post");

        user.setId(1L);
        user.setUsername("name");
        user.setEmail("name@gmail.com");

        postWithUser = new PostWithUser(post, user);

        comment.setCommentId(1L);
        comment.setText("comment");
        commentWithDetails = new CommentWithDetails(comment, user, postWithUser);

    }

    private static String createPostInJson(String title, String description) {
        return "{ \"title\": \"" + title + "\", " +
                "\"description\":\"" + description + "\"}";
    }

    private static String createCommentInJson(String text) {
        return "{ \"text\": \"" + text + "\"}";
    }

    @Test
    public void createPost_Post_Success() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/").header("username", "username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPostInJson("title", "description"));


        when(postService.createPost((anyString()), any())).thenReturn(postWithUser);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"postId\":1,\"title\":\"title\",\"description\":\"post\",\"user\":{\"id\":1,\"username\":\"name\",\"email\":\"name@gmail.com\"}}"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void deletePost_PostId_Success() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/1").header("username", "username");


        when(postService.deletePostByPostId(anyString(),anyLong())).thenReturn(1L);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andReturn();
    }

    @Test
    public void listPosts_List_Success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/list")
                .contentType(MediaType.APPLICATION_JSON);

        List<PostWithUser> postsList = new ArrayList<>();
        postsList.add(postWithUser);

        when(postService.listPosts()).thenReturn(postsList);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"postId\":1,\"title\":\"title\",\"description\":\"post\",\"user\":{\"id\":1,\"username\":\"name\",\"email\":\"name@gmail.com\"}}]"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

//    @Test
//    public void UpdatePost_Post_Success() throws Exception{
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .put("/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(createPostInJson("updated title", "body"));
//
//        Post updatedPost = post;
//        updatedPost.setTitle("updated title");
//
//        when(postService.updatePost(any(), any())).thenReturn(post);
//
//        MvcResult result = mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"postId\":1,\"title\":\"updated title\",\"description\":\"post\"}"))
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }

    @Test
    public void GetComments_List_Success() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/1/comment")
                .contentType(MediaType.APPLICATION_JSON);


        List<CommentWithDetails> commentsList = new ArrayList<>();
        commentsList.add(commentWithDetails);

        when(commentService.getCommentsByPostId((any()))).thenReturn(commentsList);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"text\":\"comment\",\"user\":{\"id\":1,\"username\":\"name\",\"email\":\"name@gmail.com\"},\"post\":{\"postId\":1,\"title\":\"title\",\"description\":\"post\",\"user\":{\"id\":1,\"username\":\"name\",\"email\":\"name@gmail.com\"}}}]"))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

}
