package com.example.postsapi.service;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.CommentWithDetails;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.PostWithUser;
import com.example.postsapi.model.User;
import com.example.postsapi.rabbitmq.Sender;
import com.example.postsapi.repository.PostCommentRepository;
import com.example.postsapi.repository.PostRepository;
import com.example.postsapi.repository.PostUserRepository;
import com.example.postsapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StreamUtils;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private Sender sender;

  @Autowired
  private PostCommentRepository postCommentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostUserRepository postUserRepository;

  @Override
  public Comment createComment(String username, Long postId, Comment text) {
    Comment comment = sender.createComment(username, postId, text);
    return comment;
  }

  @Override
  public List<CommentWithDetails> getCommentsByPostId(Long postId) throws JsonProcessingException {
    List<Comment> commentList = postCommentRepository.getCommentsByPostId(postId);
    List<Long> commentIdList = commentList.stream().map(Comment::getCommentId)
        .collect(Collectors.toList());
    Map<Long, Long> commentIdToUserId = postCommentRepository
        .findCommentIdsToUserIds(commentIdList);
    Map<Long, Long> commentIdToPostId = postCommentRepository
        .findPostIdsByCommentIds(commentIdList);
    ;
    List<Long> commentAuthorId = new ArrayList<>(commentIdToUserId.values());
    List<Long> postIdList = new ArrayList<>(commentIdToPostId.values());
    Iterable<Post> postIter = postRepository.findAllById(postIdList);
    List<Post> postList = StreamSupport.stream(postIter.spliterator(), false)
        .collect(Collectors.toList());

    //TODO
    List<CommentWithDetails> commentWithDetails = new ArrayList<>();
    for (int i = 0; i < commentList.size(); i++) {
      Comment comment_i = commentList.get(i);
      Long postId_i = commentIdToPostId.get(comment_i.getCommentId());
      Long userId_i = commentIdToUserId.get(comment_i.getCommentId());
      User commentAuthor = userRepository.findByUserId(userId_i);
      Post post_i = postList.get(postIdList.indexOf(postId_i));
      Long postAuthorId = postUserRepository.getUserIdByPostId(post_i.getPostId());
      User postAuthor = userRepository.findByUserId(postAuthorId);
      commentWithDetails.add(
          new CommentWithDetails(comment_i, commentAuthor, new PostWithUser(post_i, postAuthor)));
    }
    return commentWithDetails;
  }
}
