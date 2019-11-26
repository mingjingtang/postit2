package com.example.postsapi.repository;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class PostUserRepositoryTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    @InjectMocks
    private Post post;

    @InjectMocks
    private User user;

    @InjectMocks
    private Comment comment;

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
}
