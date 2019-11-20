package com.example.usersapi.controller;

import com.example.usersapi.model.wrapper.RoleWithUsers;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.wrapper.UserWithRoles;
import com.example.usersapi.service.UserRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRoleController {

  @Autowired
  private UserRoleService userRoleService;

  @GetMapping("/role")
  public UserWithRoles getRoleByUsername(@RequestHeader("username") String username) {
    return userRoleService.getRoleByUsername(username);
  }

  @PostMapping("/role")
  public List<UserRole> createRole(@RequestHeader("username") String username, @RequestBody UserRole userRole){
    return userRoleService.createRole(userRole);
  }

  @DeleteMapping("/role/{roleName}")
  public List<UserRole> deleteRole(@RequestHeader("username") String username, @PathVariable String roleName){
    return userRoleService.deleteRole(roleName);
  }

  @GetMapping("/roles")
  public List<UserRole> listAllRoles(@RequestHeader("username") String username){
    return userRoleService.listAllRoles();
  }

  @GetMapping("/role/{roleName}")
  public RoleWithUsers findUsersByRole(@RequestHeader("username") String username, @PathVariable String roleName){
    return userRoleService.findUsersByRole(roleName);
  }

  @PostMapping("/role/{userId}/{roleName}")
  public UserWithRoles assginRoleToUser(@RequestHeader("username") String username,
      @PathVariable Long userId, @PathVariable String roleName) {
    return userRoleService.assginRoleToUser(roleName, userId);
  }

  @DeleteMapping("/role/{userId}/{roleName}")
  public UserWithRoles removeRoleFromUser(@RequestHeader("username") String username,
      @PathVariable Long userId, @PathVariable String roleName) {
    return userRoleService.removeRoleFromUser(roleName, userId);
  }


}