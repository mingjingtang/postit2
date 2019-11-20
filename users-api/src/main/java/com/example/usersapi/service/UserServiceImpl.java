package com.example.usersapi.service;

import com.example.usersapi.config.JwtUtil;
import com.example.usersapi.model.*;
import com.example.usersapi.model.wrapper.CommentWithDetails;
import com.example.usersapi.model.wrapper.PostWithUser;
import com.example.usersapi.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private ProfileRepository profileRepository;

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  private PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public String login(User user) {
    User newUser = userRepository.findByEmail(user.getEmail());

    if (newUser != null && bCryptPasswordEncoder
        .matches(user.getPassword(), newUser.getPassword())) {
      UserDetails userDetails = loadUserByUsername(newUser.getUsername());
      return jwtUtil.generateToken(userDetails);
    }
    return null;
  }

  @Override
  public String signup(User newUser) {
    String defaultRoleName="ROLE_USER";
    UserRole userRole = roleRepository.findByName(defaultRoleName);

    newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

    User createdUser = userRepository.save(newUser);
    if ( createdUser!=null && userRoleRepository.save(createdUser.getId(), userRole.getId())==1) {
      UserDetails userDetails = loadUserByUsername(newUser.getUsername());
      return jwtUtil.generateToken(userDetails);
    }
    return null;
  }

  @Override
  public List<User> listUsers() {

    Iterable<User> userIter = userRepository.findAll();
    return StreamSupport.stream(userIter.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public Long findIdByUsername(String username) {
    User user = userRepository.findByUsername(username);
    return user.getId();
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User findById(Long userId) {
    return userRepository.findById(userId).orElse(null);
  }

  @Override
  public List<PostWithUser> getPostsByUser(String username) {
    User user = userRepository.findByUsername(username);
    Long userId = user.getId();
    List<Post> postlist = postRepository.findPostsByUserId(userId);
    List<PostWithUser> postListWithUser = new ArrayList<>();
    postlist.forEach(post -> postListWithUser.add(new PostWithUser(post, user)));
    return postListWithUser;
  }

  @Override
  public List<User> findUsersByIds(List<Long> userIdList) {
    return userRepository.findUsersByIds(userIdList);
  }

  @Override
  public List<CommentWithDetails> getCommentsByUser(String username)
      throws JsonProcessingException {
    User user = userRepository.findByUsername(username);
    Long userId = user.getId();
    List<Comment> commentList = commentRepository.findCommentsByUserId(userId);
    List<Long> commentIdList = commentList.stream().map(Comment::getCommentId)
        .collect(Collectors.toList());
    Map<Long, Long> commentIdToPostId = commentRepository
        .findPostIdsByCommentIds(commentIdList);
    List<Long> postIdList = commentIdToPostId.values().stream().collect(Collectors.toList());
    List<Post> postList = postRepository.findPostsByPostIds(postIdList);
    Map<Long, Long> postIdToUserId = postRepository.findUserIdsByPostIds(postIdList);
    List<CommentWithDetails> commentWithDetailsList = new ArrayList<>();
    for (int i = 0; i < commentList.size(); i++) {
      Comment comment_i = commentList.get(i);
      Long postId = commentIdToPostId.get(comment_i.getCommentId());
      Post post = postList.stream().filter(p -> p.getPostId() == postId).findFirst().orElse(null);
      Long authorId = postIdToUserId.get(post.getPostId());
      User author = userRepository.findById(authorId).orElse(null);
      commentWithDetailsList.add(new CommentWithDetails(comment_i, user, post, author));
    }
    return commentWithDetailsList;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User null");
    }

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        bCryptPasswordEncoder.encode(user.getPassword()), true, true, true, true,
        new ArrayList<>());
  }

}
