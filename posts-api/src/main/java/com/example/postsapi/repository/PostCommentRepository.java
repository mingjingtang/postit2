package com.example.postsapi.repository;

import com.example.postsapi.model.Comment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostCommentRepository {
    @Autowired
    private AmqpTemplate amqpTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    public List<Comment> getCommentsByPostId(Long postId) {
        String message = "getCommentsByPostId:" + postId;
        System.out.println("Sending message: " + message);
        String commentListJson = (String) amqpTemplate.convertSendAndReceive("getCommentsByPostId", message);

        List<Comment> commentList = new ArrayList<>();
        try {
            commentList = mapper.readValue(commentListJson, new TypeReference<List<Comment>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commentList;
    }
}
