package com.example.commentsapi.messagingqueue.sender;

import com.example.commentsapi.model.PostWithUser;
import javax.persistence.EntityNotFoundException;

public interface PostSender {

  public PostWithUser findByPostId(Long postId) throws EntityNotFoundException;
}
