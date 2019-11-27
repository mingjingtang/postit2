package com.example.postsapi.repository;


import com.example.postsapi.controller.PostController;
import com.example.postsapi.messagingqueue.sender.CommentSender;
import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CommentRepositoryTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    @InjectMocks
    CommentRepository commentRepository;

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
    public void createComment_Comment_Success() throws Exception {
        when(commentSender.createComment(anyString(), anyLong(), any())).thenReturn(comment);
        Comment result = commentRepository.createComment("name", 1L, comment);
        assertNotNull(result);
        assertEquals(result, comment);
    }

    @Test
    public void getCommentByPostId_List_Success() throws Exception {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(commentSender.getCommentsByPostId(anyLong())).thenReturn(comments);
        List<Comment> result = commentRepository.getCommentsByPostId(1L);
        assertNotNull(result);
        assertEquals(result, comments);
    }

    @Test
    public void findCommentIdsToUserIds_Map_Success() throws Exception {
        Map<Long, Long> map = new HashMap<>();
        map.put(1L, 1L);
        List<Long> commentIds = new ArrayList<>();
        commentIds.add(1L);
        when(commentSender.findCommentIdsToUserIds(anyList())).thenReturn(map);
        Map<Long, Long> result = commentRepository.findCommentIdsToUserIds(commentIds);
        assertNotNull(result);
        assertEquals(result, map);
    }

    @Test
    public void findPostIdsByCommentIds_Map_Success() throws Exception {
        Map<Long, Long> map = new HashMap<>();
        map.put(1L, 1L);
        List<Long> postIds = new ArrayList<>();
        postIds.add(1L);
        when(commentSender.findPostIdsByCommentIds(anyList())).thenReturn(map);
        Map<Long, Long> result = commentRepository.findPostIdsByCommentIds(postIds);
        assertNotNull(result);
        assertEquals(result, map);
    }

}
