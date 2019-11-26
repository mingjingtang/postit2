package com.example.postsapi.repository;

import com.example.postsapi.messagingqueue.sender.CommentSender;
import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PostUserRepositoryTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    @InjectMocks
    private Post post;

    @InjectMocks
    private User user;

    @InjectMocks
    private Comment comment;

    @Mock
    CommentSender commentSender;

    private PostWithUser postWithUser;

    private CommentWithDetails commentWithDetails;

    @Before
    public void init() {

        // dummy post
        post.setPostId(1L);
        post.setTitle("title");
        post.setDescription("post");

        user.setId(1L);
        user.setUsername("name");
        user.setEmail("name@gmail.com");

        postWithUser = new PostWithUser(post, user);

        // dummy comment
        comment.setCommentId(1L);
        comment.setText("comment");

        commentWithDetails = new CommentWithDetails(comment, user, postWithUser);
    }

    @Test
    public void save_Success(){
        when(commentSender.createComment(anyString(), anyLong(), any())).thenReturn(comment);
        Comment result = commentRepository.createComment("name", 1L, comment);
        assertNotNull(result);
        assertEquals(result, comment);
    }
}
