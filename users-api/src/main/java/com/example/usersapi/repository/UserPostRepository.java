package com.example.usersapi.repository;

import com.example.usersapi.model.Post;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserPostRepository {
    @Autowired
    private AmqpTemplate amqpTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    public List<Post> findPostsByUserId(Long userId) {
        String message = "findPostsByUserId:" + userId;
        System.out.println("Sending message: " + message);
        String postsJson = (String) amqpTemplate.convertSendAndReceive("findPostsByUserId", message);
        List<Post> postList = new ArrayList<>();
        try {
            postList = mapper.readValue(postsJson, new TypeReference<List<Post>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("postList not found");
        }
        return postList;
    }
}
