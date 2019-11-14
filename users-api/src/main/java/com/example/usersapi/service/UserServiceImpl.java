package com.example.usersapi.service;

import com.example.usersapi.config.JwtUtil;
import com.example.usersapi.model.PostWithDetails;
import com.example.usersapi.model.User;
import com.example.usersapi.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
//    newUser.setUserRole("ROLE_USER");
    newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

    if (userRepository.save(newUser) != null) {
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
  public List<PostWithDetails> getPostsByUser(String username) {
    return null;
  }

  @Override
  public List<User> findUsersByIds(List<Long> userIdList) {
    return userRepository.findUsersByIds(userIdList);
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

//  private List<GrantedAuthority> getGrantedAuthorities(User user) {
//    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//
//    authorities.add(new SimpleGrantedAuthority(user.getUserRole()));
//
//    return authorities;
//  }
}
