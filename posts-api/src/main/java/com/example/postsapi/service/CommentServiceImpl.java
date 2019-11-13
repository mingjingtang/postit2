package com.example.postsapi.service;

import com.example.postsapi.model.Comment;
import com.example.postsapi.rabbitmq.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private Sender sender;

    @Override
    public Comment createComment(String username, Long postId, Comment text){
        Comment comment = sender.createComment(username, postId, text);
        return comment;
    }
}
