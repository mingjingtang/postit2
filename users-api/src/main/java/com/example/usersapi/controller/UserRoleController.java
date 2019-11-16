package com.example.usersapi.controller;

import com.example.usersapi.model.UserRole;
import com.example.usersapi.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class UserRoleController {
    @Autowired
    UserRoleService roleService;

    @PostMapping
    public UserRole createRole(@RequestBody UserRole role) {
        return roleService.createRole(role);
    }

//    @GetMapping("/{rolename}")
//    public UserRole getRole(@PathVariable String rolename) {
//        return roleService.getRole(rolename);
//    }
}
