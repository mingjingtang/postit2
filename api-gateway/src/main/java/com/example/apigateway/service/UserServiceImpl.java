package com.example.apigateway.service;

import com.example.apigateway.config.JwtUtil;
import com.example.apigateway.model.User;
import com.example.apigateway.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  @Qualifier("encoder")
  private PasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByUsername(username);

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