package com.example.postsapi.rabbitmq;

import com.example.postsapi.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    @Autowired
    private AmqpTemplate amqpTemplate;

//  @Autowired
//  private AmqpAdmin amqpAdmin;

//  @PostConstruct
//  public void setUpQueue() {
//    this.amqpAdmin.declareQueue(new Queue(DIRECT_REPLY_QUEUE));
//  }
//
//  public String sendAndReceive(String message) {
//    System.out.println("Sending message: " + message);
//    return (String) rabbitTemplate.convertSendAndReceive(DIRECT_REPLY_QUEUE, message);
//  }

    private ObjectMapper mapper = new ObjectMapper();

    public Comment createComment(String username, Long postId, Comment text){
        String message = "addCommentToPost:" + username + ":" + postId + ":" + text;
        System.out.println("Sending message: " + message);
        String commentJson = (String) amqpTemplate.convertSendAndReceive("createComment", message);
        System.out.println(commentJson);
        Comment comment = null;
        try{
            comment = mapper.readValue(commentJson, Comment.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        return comment;
    }

}
