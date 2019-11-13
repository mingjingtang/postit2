package com.example.postsapi.listener;

import com.example.postsapi.model.PostWithUser;
import com.example.postsapi.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostListener {


    @Autowired
    private PostService postService;

    private ObjectMapper mapper = new ObjectMapper();


    @RabbitListener(queuesToDeclare = @Queue("findByPostId"))
    public String handleMessage_findByPostId(String message) throws JsonProcessingException {
        System.out.println("received:" + message);
        String postJson = "";
        if (message.startsWith("findByPostId")) {
            Long postId = Long.parseLong(message.split(":")[1]);
            PostWithUser post = postService.findByPostId(postId);
            postJson = mapper.writeValueAsString(post);
            return postJson;
        }
        return "";
    }
}
