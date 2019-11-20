package com.example.commentsapi.messagingqueue.sender;

import com.example.commentsapi.model.PostWithUser;

public interface PostSender {

  public PostWithUser findByPostId(Long postId);
}
