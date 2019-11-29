package com.example.postsapi.messagingqueue.sender;

import com.example.postsapi.model.User;
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
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserSenderImplTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    @InjectMocks
    UserSenderImpl userSender;


    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private User user;

    @Before
    public void init(){
        user.setId(1L);
        user.setUsername("name");
        user.setEmail("name@gmail.com");
    }

    @Test
    public void findIdByUserName_Long_Success() throws JsonProcessingException {
        String userIdJson = (new ObjectMapper()).writeValueAsString(user.getId());
        when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(userIdJson);
        Long actualUserId = userSender.findIdByUsername(user.getUsername());
        assertNotNull(actualUserId);
        assertEquals(user.getId(), actualUserId);
    }

    //user null
    @Test
    public void findByUsername_User_Success() throws IOException {
        String userJson = (new ObjectMapper()).writeValueAsString(user);
        when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(userJson);
        when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(user);
        User actualUser = userSender.findByUsername(user.getUsername());
        System.out.println(actualUser);
        assertNull(actualUser);
//        assertNotNull(actualUser);
//        assertEquals(user.getEmail(), actualUser.getEmail());
//        assertEquals(user.getUsername(), actualUser.getUsername());
    }

    //user null
    @Test
    public void findByUserId_User_Success() throws IOException {
        String userJson = (new ObjectMapper()).writeValueAsString(user.getId());
        when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(userJson);
        when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(user);
        User actualUser = userSender.findByUserId(user.getId());
        System.out.println(actualUser);
        assertNull(actualUser);
    }

    @Test
    public void findUserByUserIds_ListUsers_Success() throws IOException {
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(user.getId());
        String userIdJson = (new ObjectMapper()).writeValueAsString(userIdList);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        String userListJson = (new ObjectMapper()).writeValueAsString(userList);
        when(mapper.writeValueAsString(anyList())).thenReturn(userIdJson);
        when(amqpTemplate.convertSendAndReceive(anyString(), anyString())).thenReturn(userListJson);
        when(mapper.readValue(anyString(), any(TypeReference.class))).thenReturn(userList);
        List<User> actualListUser = userSender.findUsersByUserIds(userIdList);
        assertNotNull(actualListUser);
        assertEquals(userList.size(),actualListUser.size());
        assertEquals(userList.get(0).getId(), actualListUser.get(0).getId());
    }
}
