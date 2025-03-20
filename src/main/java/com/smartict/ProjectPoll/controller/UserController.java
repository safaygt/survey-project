package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.service.AdminService;
import com.smartict.ProjectPoll.service.UserService; // UserService'i ekledik
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@CrossOrigin
public class UserController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService; // UserService'i enjekte ettik

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        adminService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String username, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = adminService.updateUser(username, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = adminService.updateUser(userDTO); // UserService'den updateUser'ı çağırdık
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO userDTO = adminService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
        adminService.deleteUser(); // UserService'den deleteUser'ı çağırdık
        return ResponseEntity.noContent().build();
    }
}