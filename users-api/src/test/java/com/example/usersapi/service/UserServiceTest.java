package com.example.usersapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.example.usersapi.config.JwtUtil;
import com.example.usersapi.exception.LoginException;
import com.example.usersapi.exception.SignUpException;
import com.example.usersapi.model.Comment;
import com.example.usersapi.model.Post;
import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.wrapper.CommentWithDetails;
import com.example.usersapi.model.wrapper.PostWithUser;
import com.example.usersapi.repository.CommentRepository;
import com.example.usersapi.repository.PostRepository;
import com.example.usersapi.repository.RoleRepository;
import com.example.usersapi.repository.UserRepository;
import com.example.usersapi.repository.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

public class UserServiceTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  UserServiceImpl userService;

  @InjectMocks
  private User user;

  @InjectMocks
  private UserRole userRole;

  @InjectMocks
  private Post post;

  @InjectMocks
  private Comment comment;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private UserRoleRepository userRoleRepository;

  @Mock
  private PostRepository postRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private PasswordEncoder bCryptPasswordEncoder;

  @Mock
  private JwtUtil jwtUtil;

  List<User> users;

  List<Post> posts;

  List<Comment> comments;

  @Before
  public void init() {

    user.setId(1L);
    user.setEmail("testEmail@email.com");
    user.setUsername("testUser");
    user.setPassword("testPass");

    userRole.setId(1L);
    userRole.setName("ROLE_USER");

    users = new ArrayList<>();
    users.add(user);

    posts = new ArrayList<>();
    comments = new ArrayList<>();
  }

  @Test
  public void signup_ReturnToken_Success() throws SignUpException {

    String expectedToken = "returnToken";

    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRepository.findByUsername(anyString())).thenReturn(null).thenReturn(user);
    when(userRepository.findByEmail(anyString())).thenReturn(null);
    when(userRepository.save(any())).thenReturn(user);
    when(userRoleRepository.save(anyLong(), anyLong())).thenReturn(1);
    when(jwtUtil.generateToken(any())).thenReturn(expectedToken);
    when(bCryptPasswordEncoder.encode(anyString())).thenReturn("testPass");

    String actualToken = userService.signup(user);

    assertEquals(expectedToken, actualToken);
  }

  @Test(expected = SignUpException.class)
  public void signup_duplicateUsername_SignUpException() throws SignUpException {

    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRepository.findByUsername(anyString())).thenReturn(user);

    userService.signup(user);
  }

  @Test(expected = SignUpException.class)
  public void signup_duplicateEmail_SignUpException() throws SignUpException {

    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRepository.findByUsername(anyString())).thenReturn(null);
    when(userRepository.findByEmail(anyString())).thenReturn(user);
    userService.signup(user);
  }

  @Test(expected = SignUpException.class)
  public void signup_saveUserFailed_SignUpException() throws SignUpException {

    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRepository.findByUsername(anyString())).thenReturn(null);
    when(userRepository.findByEmail(anyString())).thenReturn(null);
    when(userRepository.save(any())).thenReturn(null);
    userService.signup(user);
  }

  @Test(expected = SignUpException.class)
  public void signup_saveUserRoleFailed_SignUpException() throws SignUpException {

    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRepository.findByUsername(anyString())).thenReturn(null);
    when(userRepository.findByEmail(anyString())).thenReturn(null);
    when(userRepository.save(any())).thenReturn(user);
    when(userRoleRepository.save(anyLong(), anyLong())).thenReturn(0);
    userService.signup(user);
  }

  @Test
  public void login_ReturnToken_Success() throws LoginException {
    String expectedToken = "returnToken";
    when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
    when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(jwtUtil.generateToken(any())).thenReturn(expectedToken);
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(bCryptPasswordEncoder.encode(anyString())).thenReturn("testPass");

    String actualToken = userService.login(user);
    assertEquals(expectedToken, actualToken);
  }

  @Test(expected = LoginException.class)
  public void login_NullEmail_LoginException() throws LoginException {
    User user = new User();
    userService.login(user);
  }

  @Test(expected = LoginException.class)
  public void login_EmptyEmail_LoginException() throws LoginException {
    User user = new User();
    user.setEmail("");
    userService.login(user);
  }

  @Test(expected = LoginException.class)
  public void login_NullPassword_LoginException() throws LoginException {
    User user = new User();
    user.setEmail("email");
    userService.login(user);
  }

  @Test(expected = LoginException.class)
  public void login_EmptyPassword_LoginException() throws LoginException {
    User user = new User();
    user.setEmail("email");
    user.setPassword("");
    userService.login(user);
  }

  @Test(expected = LoginException.class)
  public void login_UserNotFound_LoginException() throws LoginException {
    when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
    userService.login(user);
  }

  @Test(expected = LoginException.class)
  public void login_PasswordNotMatch_LoginException() throws LoginException {
    when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
    when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);
    userService.login(user);
  }

  @Test
  public void listUsers_ListOfUser_Success() {
    Iterable<User> userIter = users;
    when(userRepository.findAll()).thenReturn(userIter);

    List<User> actualUsers = userService.listUsers();
    assertEquals(users, actualUsers);
  }

  @Test
  public void findIdByUsername_UserId_Success() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    Long actualId = userService.findIdByUsername(user.getUsername());
    assertEquals(user.getId(), actualId);
  }

  @Test
  public void findByEmail_User_Success() {
    when(userRepository.findByEmail(anyString())).thenReturn(user);
    User actualUser = userService.findByEmail(user.getEmail());
    assertEquals(user, actualUser);
  }

  @Test
  public void findByUsername_User_Success() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    User actualUser = userService.findByUsername(user.getEmail());
    assertEquals(user, actualUser);
  }

  @Test
  public void findById_User_Success() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    User actualUser = userService.findById(user.getId());
    assertEquals(user, actualUser);
  }

  @Test
  public void getPostsByUser_ListOfPostWithUser_Success() {
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("description");
    posts.add(post);
    List<PostWithUser> postWithUsersList = new ArrayList<>();
    posts.forEach(thisPost -> postWithUsersList.add(new PostWithUser(thisPost, user)));
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(postRepository.findPostsByUserId(anyLong())).thenReturn(posts);

    List<PostWithUser> actualPostWithUsersList = userService.getPostsByUser(user.getUsername());
    assertEquals(actualPostWithUsersList.size(), postWithUsersList.size());
    assertEquals(actualPostWithUsersList.get(0).getPostId(), postWithUsersList.get(0).getPostId());
  }

  @Test
  public void findUsersByIds_ListOfUsers() {
    when(userRepository.findUsersByIds(any())).thenReturn(users);

    List<Long> userIdList = new ArrayList<>();
    userIdList.add(1L);
    List<User> actualUsers = userService.findUsersByIds(userIdList);
    assertEquals(users, actualUsers);
  }

  @Test
  public void getCommentsByUser_ListOfCommentWithDetails_Success() throws JsonProcessingException {
    comment.setCommentId(1L);
    comment.setText("comment");
    comments.add(comment);
    List<Long> commentIdList = comments.stream().map(Comment::getCommentId)
        .collect(Collectors.toList());
    Map<Long, Long> commentIdToPostId = new HashMap<>();
    commentIdToPostId.put(1L, 1L);
    List<Long> postIdList = commentIdToPostId.values().stream().collect(Collectors.toList());
    post.setPostId(1L);
    post.setTitle("title");
    post.setDescription("description");
    posts.add(post);
    Map<Long, Long> postIdToUserId = new HashMap<>();
    postIdToUserId.put(1L, 1L);

    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(commentRepository.findCommentsByUserId(anyLong())).thenReturn(comments);
    when(commentRepository.findPostIdsByCommentIds(commentIdList)).thenReturn(commentIdToPostId);
    when(postRepository.findPostsByPostIds(any())).thenReturn(posts);
    when(postRepository.findUserIdsByPostIds(postIdList)).thenReturn(postIdToUserId);
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    List<CommentWithDetails> commentWithDetailsList = userService
        .getCommentsByUser(user.getUsername());
    assertEquals(1, commentWithDetailsList.size());
    assertEquals(comment.getCommentId(), commentWithDetailsList.get(0).getId());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_UserNotFound_UsernameNotFoundException() {
    when(userRepository.findByUsername(anyString())).thenReturn(null);
    userService.loadUserByUsername(user.getUsername());
  }
}


