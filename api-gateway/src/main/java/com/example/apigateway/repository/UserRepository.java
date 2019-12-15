package com.example.apigateway.repository;

import com.example.apigateway.model.User;
import com.example.apigateway.model.UserWithRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user")
public interface UserRepository {

  @GetMapping("/auth/{username}/{authkey}")
  public UserWithRoles findUserWithRolesByUsername(@PathVariable String username, @PathVariable String authkey);
}