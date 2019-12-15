package com.example.usersapi.controller;

import com.example.usersapi.model.wrapper.UserWithRoles;
import com.example.usersapi.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Autowired
  private UserRoleService userRoleService;

  @GetMapping("/auth/{username}/{authkey}")
  public UserWithRoles findUserWithRolesByUsername(@PathVariable String username, @PathVariable String authkey){
    if(!"auth-verification-13772384292".equals(authkey)){
      return null;
    }
    return userRoleService.getRoleByUsername(username);
  }
}
