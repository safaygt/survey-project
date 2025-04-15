package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*") // CrossOrigin ekle
public class UserController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO userDTO = adminService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }


}