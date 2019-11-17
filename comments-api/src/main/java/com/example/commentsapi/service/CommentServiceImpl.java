package com.example.commentsapi.service;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.model.CommentWithDetails;
import com.example.commentsapi.model.PostWithUser;
import com.example.commentsapi.model.User;
import com.example.commentsapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private CommentPostRepository commentPostRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentUserRepository commentUserRepository;

  @Override
  public CommentWithDetails createComment(String username, Long postId, Comment comment) {
    User user = userRepository.findByUsername(username);
    PostWithUser postWithUser = postRepository.findByPostId(postId);
    Comment savedComment = commentRepository.save(comment);

    if (savedComment != null && commentPostRepository.save(postId, savedComment.getCommentId()) == 1
        && commentUserRepository.save(user.getId(), savedComment.getCommentId()) == 1) {
      CommentWithDetails commentWithDetails = new CommentWithDetails();
      commentWithDetails.setId(savedComment.getCommentId());
      commentWithDetails.setText(savedComment.getText());
      commentWithDetails.setUser(user);
      commentWithDetails.setPost(postWithUser);
      return commentWithDetails;
    }

    return null;
  }

  @Override
  public Long deleteComment(Long commentId) {
    Comment comment = commentRepository.findById(commentId).orElse(null);
    if (comment != null) {
      commentRepository.delete(comment);
      commentPostRepository.delete(commentId);
      commentUserRepository.delete(commentId);
      return commentId;
    }
    return 0L;
  }
}
