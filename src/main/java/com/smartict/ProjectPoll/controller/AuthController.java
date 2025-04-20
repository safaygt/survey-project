package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.JwtResponse;
import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.jwt.JwtUtil;
import com.smartict.ProjectPoll.service.RoleService;
import com.smartict.ProjectPoll.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserDTO userDTO) {
        JwtResponse jwtResponse = new JwtResponse();
        try {
            UserDTO user = userService.login(userDTO, authenticationManager);
            String role = roleService.getRoleTextById(user.getRole().name()); // Düzeltilmiş satır
            String token = jwtUtil.generateToken(user.getUsername(), role, user.getId());
            Integer userId = userService.findUserIdByUsername(userDTO.getUsername());
            jwtResponse.setToken(token);
            jwtResponse.setUserId(userId);
            jwtResponse.setUsername(user.getUsername());
            jwtResponse.setResponseCode(HttpStatus.OK);
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


