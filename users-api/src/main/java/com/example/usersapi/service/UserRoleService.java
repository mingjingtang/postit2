package com.example.usersapi.service;

import com.example.usersapi.model.wrapper.RoleWithUsers;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.wrapper.UserWithRoles;
import java.util.List;

public interface UserRoleService {

  public UserWithRoles getRoleByUsername(String username);

  public UserWithRoles assginRoleToUser(String roleName, Long userId);

  public List<UserRole> listAllRoles();

  public RoleWithUsers findUsersByRole(String roleName);

  public UserWithRoles removeRoleFromUser(String roleName, Long userId);

  public List<UserRole> createRole(UserRole userRole);

  public List<UserRole> deleteRole(String roleName);
}
