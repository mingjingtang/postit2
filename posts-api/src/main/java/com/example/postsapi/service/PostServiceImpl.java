package com.example.postsapi.service;

import com.example.postsapi.model.Comment;
import com.example.postsapi.model.Post;
import com.example.postsapi.model.PostWithUser;
import com.example.postsapi.model.User;
import com.example.postsapi.repository.PostRepository;
import com.example.postsapi.repository.PostUserRepository;
import com.example.postsapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostUserRepository postUserRepository;

    @Override
    public PostWithUser createPost(String username, Post post) {
        Post savedPost = postRepository.save(post);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        postUserRepository.save(user.getId(), savedPost.getPostId());
        PostWithUser postWithUser = new PostWithUser(savedPost, user);
        return postWithUser;
    }

    @Override
    public Long deletePostByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return 0L;
        }
        postRepository.delete(post);
        return post.getPostId();
    }

    @Override
    public List<Post> listPosts() {
        Iterable<Post> postIterable = postRepository.findAll();
        return StreamSupport.stream(postIterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Post updatePost(Long postId, Post post){
        Post newPost = postRepository.findById(postId).get();
        newPost.setTitle(post.getTitle());
        newPost.setDescription(post.getDescription());
        postRepository.save(newPost);
        return newPost;
    }

    @Override
    public PostWithUser findByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        Long userId = postUserRepository.getUserIdByPostId(postId);
        User user = userRepository.findByUserId(userId);
        PostWithUser postWithUser = new PostWithUser(post, user);
        return postWithUser;
    }
}
