package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.JwtResponse;
import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(@Lazy UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.register(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        JwtResponse jwtResponse = new JwtResponse();
        try {
            String token = userService.login(userDTO, authenticationManager);
            Integer userId = userService.findUserIdByUsername(userDTO.getUsername()); // Kullanıcı ID'sini al
            String username = userDTO.getUsername(); // Kullanıcı adını al

            jwtResponse.setToken(token);
            jwtResponse.setUserId(userId); // Kullanıcı ID'sini ayarla
            jwtResponse.setUsername(username); // Kullanıcı adını ayarla
            jwtResponse.setResponseCode(HttpStatus.OK);
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an error!");
        }
    }
}