package com.example.usersapi.service;

import com.example.usersapi.model.wrapper.RoleWithUsers;
import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.wrapper.UserWithRoles;
import com.example.usersapi.repository.RoleRepository;
import com.example.usersapi.repository.UserRepository;
import com.example.usersapi.repository.UserRoleRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Override
  public UserWithRoles getRoleByUsername(String username) {
    User user = userRepository.findByUsername(username);
    List<UserRole> roles = userRoleRepository.findRolesByUserId(user.getId());
    return new UserWithRoles(user, roles);
  }

  @Override
  public UserWithRoles assginRoleToUser(String roleName, Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (userId == null) {
      throw new RuntimeException("user not found");
    }
    UserRole userRole = roleRepository.findByName(roleName);
    List<UserRole> roles = userRoleRepository.findRolesByUserId(userId);
    if (roles.contains(userRole)) {
      System.out.println("duplicate role for the same user");
      return new UserWithRoles(user, roles);
    }
    if (userRoleRepository.save(user.getId(), userRole.getId()) == 1) {
      roles.add(userRole);
      return new UserWithRoles(user, roles);
    } else {
      throw new RuntimeException("user role cannot be added: save relationship failed");
    }
  }

  @Override
  public List<UserRole> listAllRoles() {
    Iterable<UserRole> roleIter = roleRepository.findAll();
    List<UserRole> roles = StreamSupport.stream(roleIter.spliterator(), false).collect(Collectors.toList());
    return roles;
  }

  @Override
  public RoleWithUsers findUsersByRole(String roleName) {
    UserRole role = roleRepository.findByName(roleName);
    if(role == null) {
      throw new RuntimeException("userrole not found: " + roleName);
    }
    List<User> users = userRoleRepository.findUserByRoleId(role.getId());
    return new RoleWithUsers(role, users);
  }

  @Override
  public UserWithRoles removeRoleFromUser(String roleName, Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (userId == null) {
      throw new RuntimeException("user not found");
    }
    UserRole userRole = roleRepository.findByName(roleName);
    List<UserRole> roles = userRoleRepository.findRolesByUserId(userId);
    if (!roles.contains(userRole)) {
      System.out.println("user " + user.getUsername() + " doesn't have this role");
      return new UserWithRoles(user, roles);
    }
    if (userRoleRepository.delete(user.getId(), userRole.getId()) == 1) {
      roles.remove(userRole);
      return new UserWithRoles(user, roles);
    } else {
      throw new RuntimeException("user role cannot be added: save relationship failed");
    }
  }

  @Override
  public List<UserRole> createRole(UserRole userRole) {
    List<UserRole> roles = listAllRoles();
    if(roles.stream().anyMatch(role->role.getName().equals(userRole.getName()))==false){
      UserRole savedUserRole = roleRepository.save(userRole);
      roles.add(savedUserRole);
    }
    return roles;
  }

  @Override
  public List<UserRole> deleteRole(String roleName) {
    UserRole role = roleRepository.findByName(roleName);
    roleRepository.delete(role);
    List<UserRole> roles = listAllRoles();
    return roles;
  }
}
