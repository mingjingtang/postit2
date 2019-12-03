package com.example.usersapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserRole;
import com.example.usersapi.model.wrapper.RoleWithUsers;
import com.example.usersapi.model.wrapper.UserWithRoles;
import com.example.usersapi.repository.RoleRepository;
import com.example.usersapi.repository.UserRepository;
import com.example.usersapi.repository.UserRoleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class UserRoleServiceTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule().silent();

  @InjectMocks
  private UserRoleServiceImpl userRoleService;

  @InjectMocks
  private User user;

  @InjectMocks
  private UserRole userRole;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private UserRoleRepository userRoleRepository;

  private UserWithRoles userWithRoles;

  private RoleWithUsers roleWithUsers;

  List<User> users;

  List<UserRole> roles;

  @Before
  public void init() {
    user.setId(1L);
    user.setEmail("email1@email.com");
    user.setUsername("user1");
    user.setPassword("pwd1");

    userRole.setId(1L);
    userRole.setName("ROLE_USER");

    users = new ArrayList<>();
    users.add(user);
    roles = new ArrayList<>();
    roles.add(userRole);

    userWithRoles = new UserWithRoles(user, roles);
    roleWithUsers = new RoleWithUsers(userRole, users);
  }

  @Test
  public void getRoleByUsername_UserWithRoles_Success() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);
    when(userRoleRepository.findRolesByUserId(anyLong())).thenReturn(roles);

    UserWithRoles actualUserWithRoles = userRoleService.getRoleByUsername(user.getUsername());

    assertEquals(userWithRoles.getRoles().size(), actualUserWithRoles.getRoles().size());
    assertEquals(userWithRoles.getRoles().get(0).getId(),
        actualUserWithRoles.getRoles().get(0).getId());
  }

  @Test
  public void assginRoleToUser_UserWithRoles_Success() {
    UserRole newUserRole = new UserRole();
    userRole.setId(2L);
    userRole.setName("ROLE_USER2");
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(roleRepository.findByName(anyString())).thenReturn(newUserRole);
    when(userRoleRepository.findRolesByUserId(anyLong())).thenReturn(roles);

    UserWithRoles actualUserWithRoles = userRoleService.assginRoleToUser("ROLE_USER", user.getId());
    assertEquals(2, actualUserWithRoles.getRoles().size());
  }

  @Test
  public void assginRoleToUser_UserWithRoles_duplicateRole() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRoleRepository.findRolesByUserId(anyLong())).thenReturn(roles);
    UserWithRoles actualUserWithRoles = userRoleService.assginRoleToUser("ROLE_USER", user.getId());
    assertEquals(1, actualUserWithRoles.getRoles().size());
  }

  @Test(expected = EntityNotFoundException.class)
  public void assginRoleToUser_UserNotFound_Success() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    userRoleService.assginRoleToUser(user.getUsername(), userRole.getId());
  }

  @Test
  public void listAllRoles_ListOfUserRole_Success() {
    Iterable<UserRole> roleIter = roles;
    when(roleRepository.findAll()).thenReturn(roleIter);
    List<UserRole> actualRoles = userRoleService.listAllRoles();
    assertEquals(roles.size(), actualRoles.size());
    assertEquals(roles.get(0).getId(), actualRoles.get(0).getId());
  }

  @Test
  public void findUsersByRole_RoleWithUsers_Success() {
    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRoleRepository.findUserByRoleId(anyLong())).thenReturn(users);

    RoleWithUsers roleWithUsers = userRoleService.findUsersByRole("xx");
    assertEquals(users.size(), roleWithUsers.getUsers().size());
    assertEquals(userRole.getId(), roleWithUsers.getId());
  }

  @Test(expected = EntityNotFoundException.class)
  public void findUsersByRole_roleNotFound_EntityNotFoundException() {
    when(roleRepository.findByName(anyString())).thenReturn(null);
    userRoleService.findUsersByRole("xx");
  }

  @Test
  public void removeRoleFromUser_UserWithRoles_Success() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRoleRepository.findRolesByUserId(anyLong())).thenReturn(roles);
    when(userRoleRepository.delete(anyLong(), anyLong())).thenReturn(1);
    UserWithRoles actualUserWithRoles = userRoleService.removeRoleFromUser("xx", 1L);
    assertEquals(0, roles.size());
  }

  @Test
  public void removeRoleFromUser_UserWithRoles_UserRoleNotFoundForThisUser() {
    roles.remove(0);
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRoleRepository.findRolesByUserId(anyLong())).thenReturn(roles);

    UserWithRoles actualUserWithRoles = userRoleService.removeRoleFromUser("xx", 1L);
    assertEquals(0, actualUserWithRoles.getRoles().size());
  }

  @Test(expected = EntityNotFoundException.class)
  public void removeRoleFromUser_userNotFound_EntityNotFoundException() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    userRoleService.removeRoleFromUser("xx", 1L);
  }

  @Test(expected = RuntimeException.class)
  public void removeRoleFromUser_deleteDataFailed_RuntimeException() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    when(userRoleRepository.findRolesByUserId(anyLong())).thenReturn(roles);
    when(userRoleRepository.delete(anyLong(), anyLong())).thenReturn(0);
    userRoleService.removeRoleFromUser("xx", 1L);
  }

  @Test
  public void createRole_ListOfRoles_Success() {
    Iterable<UserRole> roleIter = roles;
    UserRole newRole = new UserRole();
    newRole.setId(2L);
    newRole.setName("ROLE_USER2");

    when(roleRepository.findAll()).thenReturn(roleIter);
    when(roleRepository.save(any())).thenReturn(newRole);

    List<UserRole> actualRoles = userRoleService.createRole(newRole);
    assertEquals(2, actualRoles.size());
    assertEquals("ROLE_USER2", actualRoles.get(1).getName());
  }

  @Test
  public void deleteRole_ListOfRoles_Success() {
    when(roleRepository.findByName(anyString())).thenReturn(userRole);
    Iterable<UserRole> roleIter = roles;
    when(roleRepository.findAll()).thenReturn(roleIter);

    List<UserRole> actualRoles = userRoleService.deleteRole("xx");
    assertEquals(1, actualRoles.size());
  }
}
