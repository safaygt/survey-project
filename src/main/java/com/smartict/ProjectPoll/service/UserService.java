package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.entity.Roles;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.jwt.JwtUtil;
import com.smartict.ProjectPoll.mapper.UserMapper;
import com.smartict.ProjectPoll.repository.RolesRepo;
import com.smartict.ProjectPoll.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RolesRepo rolesRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepo userRepo, RolesRepo rolesRepo, PasswordEncoder passwordEncoder, @Lazy JwtUtil jwtUtil, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.rolesRepo = rolesRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    public String register(UserDTO userDTO) {
        if (userRepo.findByUsername(userDTO.getUsername()) != null) {
            return "This username is already used!";
        }

        // Varsayılan rol ID'sini ayarla
        if (userDTO.getRoleId() == null) {
            userDTO.setRoleId(5);
        }

        Usr usr = userMapper.toEntity(userDTO);
        usr.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Roles role = rolesRepo.findById(userDTO.getRoleId()).orElse(null);
        if (role == null) {
            return "Role not found";
        }
        usr.setRole(role);
        userRepo.save(usr);
        return "User has been saved successfully!";
    }

    public String login(UserDTO userDTO, AuthenticationManager authenticationManager) {
        Usr usr = userRepo.findByUsername(userDTO.getUsername());

        if (usr == null) {
            throw new BadCredentialsException("User not found: " + userDTO.getUsername());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            System.err.println("Authentication failed for user: " + userDTO.getUsername());
            throw new BadCredentialsException("Wrong username or password!");
        }

        return jwtUtil.generateToken(usr.getUsername(), usr.getRole().getRoleText(), usr.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usr usr = userRepo.findByUsername(username);
        if (usr == null) {
            throw new UsernameNotFoundException("User not found " + username);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Roles role = usr.getRole();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleText()));
        }

        return new User(usr.getUsername(), usr.getPassword(), authorities);
    }

    public Integer findUserIdByUsername(String username) {
        Usr user = userRepo.findByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        }
    }
}