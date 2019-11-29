package com.example.postsapi.messagingqueue.sender;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.amqp.core.AmqpTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CommentSenderImplTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    @InjectMocks
    private CommentSenderImpl commentSender;

    @InjectMocks
    private Comment comment;

    @InjectMocks
    User user;

    @InjectMocks
    Post post;

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private ObjectMapper mapper = new ObjectMapper();

    private List<Comment> comments;

    private PostWithUser postWithUser;

    private CommentWithDetails commentWithDetails;


    @Before
    public void init() {
        post.setPostId(1L);
        post.setTitle("title");
        post.setDescription("post");

        user.setId(1L);
        user.setUsername("name");
        user.setEmail("name@gmail.com");

        postWithUser = new PostWithUser(post, user);

        comment.setCommentId(1L);
        comment.setText("comment1");

        comments = new ArrayList<>();
        comments.add(comment);

        commentWithDetails = new CommentWithDetails(comment, user, postWithUser);
    }

    @Test
    public void createComment_Comment_Success() throws IOException {
        String commentJson = (new ObjectMapper()).writeValueAsString(comment);
        when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(commentJson);
        when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(comment);
        Comment actualComment = commentSender.createComment(user.getUsername(), post.getPostId(), comment);
        assertNull(actualComment);
    }


    @Test
    public void getCommentByPostId_ListComments_Success() throws IOException {
        String commentListJson = (new ObjectMapper()).writeValueAsString(comments);
        when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(commentListJson);
        when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(comments);
        List<Comment> actualCommentList = commentSender.getCommentsByPostId(1L);
        assertNotNull(actualCommentList);
    }


    @Test
    public void findPostIdByCommentId_Map_Success() throws IOException{
        Map<Long, Long> commentIdToPostId = new HashMap<>();
        commentIdToPostId.put(1L, 1L);
        List<Long> commentIds = new ArrayList<>(commentIdToPostId.keySet());
        String mapJson = (new ObjectMapper()).writeValueAsString(commentIdToPostId);

        when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(mapJson);
        when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(commentIdToPostId);
        when(mapper.writeValueAsString(any())).thenReturn("message");

        Map<Long, Long> actualCommentIdToPostId = commentSender.findPostIdsByCommentIds(commentIds);
        assertEquals(commentIdToPostId.get(1L), actualCommentIdToPostId.get(1L));
    }

}
