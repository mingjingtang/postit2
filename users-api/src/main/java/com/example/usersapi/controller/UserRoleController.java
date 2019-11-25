package com.example.usersapi.controller;

import com.example.usersapi.model.wrapper.RoleWithUsers;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.wrapper.UserProfileWithUser;
import com.example.usersapi.model.wrapper.UserWithRoles;
import com.example.usersapi.service.UserRoleService;
import java.util.List;

import io.swagger.annotations.ApiOperation;
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
  @ApiOperation(value = "Get userrole by username", notes = "get role", response = UserWithRoles.class)
  public UserWithRoles getRoleByUsername(@RequestHeader("username") String username) {
    return userRoleService.getRoleByUsername(username);
  }

  @PostMapping("/role")
  @ApiOperation(value = "create userrole by admin user", notes = "create role", response = UserRole.class, responseContainer = "List")
  public List<UserRole> createRole(@RequestHeader("username") String username, @RequestBody UserRole userRole){
    return userRoleService.createRole(userRole);
  }

  @DeleteMapping("/role/{roleName}")
  @ApiOperation(value = "Delete user role by admin user", notes = "delete role by pass role name", response = UserRole.class, responseContainer = "List")
  public List<UserRole> deleteRole(@RequestHeader("username") String username, @PathVariable String roleName){
    return userRoleService.deleteRole(roleName);
  }

  @GetMapping("/roles")
  @ApiOperation(value = "List all user roles", notes = "get roles", response = UserRole.class, responseContainer = "List")
  public List<UserRole> listAllRoles(@RequestHeader("username") String username){
    return userRoleService.listAllRoles();
  }

  @GetMapping("/role/{roleName}")
  @ApiOperation(value = "Find users by user role", notes = "get users by roleName ", response = RoleWithUsers.class)
  public RoleWithUsers findUsersByRole(@RequestHeader("username") String username, @PathVariable String roleName){
    return userRoleService.findUsersByRole(roleName);
  }

  @PostMapping("/role/{userId}/{roleName}")
  @ApiOperation(value = "Assgin user role to user", notes = "post role to user by passing userId and roleName", response = UserWithRoles.class)
  public UserWithRoles assginRoleToUser(@RequestHeader("username") String username,
      @PathVariable Long userId, @PathVariable String roleName) {
    return userRoleService.assginRoleToUser(roleName, userId);
  }

  @DeleteMapping("/role/{userId}/{roleName}")
  @ApiOperation(value = "Delete an user role from the user", notes = "delete role to user by passing userId and roleName", response = UserWithRoles.class)
  public UserWithRoles removeRoleFromUser(@RequestHeader("username") String username,
      @PathVariable Long userId, @PathVariable String roleName) {
    return userRoleService.removeRoleFromUser(roleName, userId);
  }


}