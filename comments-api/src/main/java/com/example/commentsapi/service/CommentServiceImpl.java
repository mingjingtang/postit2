package com.example.commentsapi.service;

import com.example.commentsapi.model.Comment;
import com.example.commentsapi.repository.CommentRepository;
import com.example.commentsapi.repository.PostCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Override
    public Comment createComment(Long postId, Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        if (savedComment != null && postCommentRepository.save(postId, savedComment.getCommentId()) == 1) {
            return comment;
        }
        return null;
    }

    @Override
    public Long deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment!= null){
            commentRepository.delete(comment);
            postCommentRepository.delete(commentId);
            return commentId;
        }
        return 0L;
    }
}
