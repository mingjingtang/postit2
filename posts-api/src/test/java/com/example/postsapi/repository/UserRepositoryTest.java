package com.example.postsapi.repository;

import com.example.postsapi.messagingqueue.sender.UserSender;
import com.example.postsapi.model.User;
import com.example.postsapi.model.wrapper.CommentWithDetails;
import com.example.postsapi.model.wrapper.PostWithUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    @Mock
    UserSender userSender;

    @InjectMocks
    private User user;

    @InjectMocks
    private UserRepository userRepository;

    @Before
    public void init() {
        user.setId(1L);
        user.setUsername("name");
        user.setEmail("name@gmail.com");
    }

    @Test
    public void findIdByUsername_Success(){
        when(userSender.findIdByUsername(anyString())).thenReturn(1L);
        Long result = userRepository.findIdByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals((long)result, (long)1L);
    }

    @Test
    public void findByUsername_Success(){
        when(userSender.findByUsername(anyString())).thenReturn(user);
        User result = userRepository.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(result, user);
    }

    @Test
    public void findByUserId_Success(){
        when(userSender.findByUserId(anyLong())).thenReturn(user);
        User result = userRepository.findByUserId(user.getId());
        assertNotNull(result);
        assertEquals(result, user);
    }

    @Test
    public void findUsersByUserIds_Success() throws JsonProcessingException {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userSender.findUsersByUserIds(anyList())).thenReturn(userList);
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(1L);
        List<User> listResult = userRepository.findUsersByUserIds(userIdList);
        assertNotNull(listResult);
        assertEquals(listResult, userList);
    }
}
