package com.ga.controller;

import com.example.postsapi.controller.PostController;
import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.service.CommentService;
import com.example.postsapi.service.PostService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class PostControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    PostController postController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private Post post;

    @InjectMocks
    private User user;

    @InjectMocks
    private Comment comment;


    @Before
    public void initializeDummyPost() {
        post.setPostId(1L);
        post.setTitle("title");
        post.setDescription("description");

        user.setId(1L);
        user.setUsername("name");
        user.setEmail("name@gmail.com");

    }

}
