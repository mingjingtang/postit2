package com.example.apigateway.service;

import com.example.apigateway.config.JwtUtil;
import com.example.apigateway.model.UserRole;
import com.example.apigateway.model.UserWithRoles;
import com.example.apigateway.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserWithRoles user = userRepository.findUserWithRolesByUsername(username, "auth-verification-13772384292");

    if (user == null) {
      logger.warn("request with username: " + username + " is not found");
      throw new UsernameNotFoundException("User null");
    }

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        "", true, true, true, true,
        getGrantedAuthorities(user));
  }

  private List<GrantedAuthority> getGrantedAuthorities(UserWithRoles user) {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    List<UserRole> roles = user.getRoles();
    for (UserRole role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }

    return authorities;
  }
}