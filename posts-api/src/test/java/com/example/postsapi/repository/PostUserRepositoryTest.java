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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
    JdbcTemplate jdbcTemplate;

    @Mock
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    PostUserRepository postUserRepository;


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
        when(jdbcTemplate.update(anyString(), anyLong(), anyLong())).thenReturn(1);
        int result = postUserRepository.save(1l, 1l);
        assertNotNull(result);
        assertEquals(result, 1L);
    }

    @Test
    public void getUserIdByPostId_Success(){
        when(jdbcTemplate.queryForObject(anyString(), (Class<Object>) any())).thenReturn(1L);
        String sql = "SELECT user_id FROM post_user WHERE post_id = " + 1L;
        Long result = postUserRepository.getUserIdByPostId(1L);
        assertNotNull(result);
        assertEquals((long)result, (long)1L);
    }

    @Test
    public void findUserIdsByPostIds_Success(){
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        List<Long> postIds = new ArrayList<>();
        postIds.add(1L);
        when(namedParameterJdbcTemplate.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(userIds);
        List<Long> result = postUserRepository.findUserIdsByPostIds(postIds);
        assertNotNull(result);
        assertEquals(result, userIds);
    }

    @Test
    public void findUserIdsByUserIds_Success(){
        List<Long> postIds = new ArrayList<>();
        postIds.add(1L);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn();
    }


//    @Test
//    public void deleteByPostId_Success(){
//        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(null);
//    }
}
