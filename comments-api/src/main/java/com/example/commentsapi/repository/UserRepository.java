package com.example.commentsapi.repository;

import com.example.commentsapi.messagingqueue.sender.UserSender;
import com.example.commentsapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private UserSender userSender;

    public User findByUsername(String username) {
        return userSender.findByUsername(username);
    }
}
