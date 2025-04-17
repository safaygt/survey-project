package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.entity.EnumRole;
import com.smartict.ProjectPoll.entity.Roles;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.jwt.JwtUtil;
import com.smartict.ProjectPoll.mapper.UserMapper;
import com.smartict.ProjectPoll.repository.RolesRepo;
import com.smartict.ProjectPoll.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RolesRepo rolesRepo;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    Logger logger = Logger.getLogger(getClass().getName());

    public UserDTO login(UserDTO userDTO, AuthenticationManager authenticationManager) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            logger.info("Authentication failed for user: " + userDTO.getUsername());
            throw new BadCredentialsException("Wrong username or password!");
        }

        Usr usr = userRepository.findByUsername(userDTO.getUsername());
        if (usr == null) {
            usr = new Usr();
            usr.setUsername(userDTO.getUsername());
            usr.setRole(rolesRepo.findByRoleText(EnumRole.NORMAL).orElseThrow(() -> new RuntimeException("Default role not found")));
            return userMapper.toDto(userRepository.save(usr));

        }
        return userMapper.toDto(usr);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usr usr = userRepository.findByUsername(username);
        if (usr == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Roles role = usr.getRole();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleText())); // Düzeltilmiş satır
        }

        return new User(usr.getUsername(), "", authorities);
    }

    public Integer findUserIdByUsername(String username) {
        Usr user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        }
    }
}