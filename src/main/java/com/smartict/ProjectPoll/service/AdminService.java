package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.mapper.UserMapper;
import com.smartict.ProjectPoll.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final UserRepository userRepository;

    private final UserMapper userMapper;



    public UserDTO getUserDTOFromUsr(Usr user) {
        return userMapper.toDto(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }


    public UserDTO getUserByUsername(String username) {
        Usr usr = userRepository.findByUsername(username);
        if (usr == null) {
            throw new EntityNotFoundException("User not found");
        }
        return userMapper.toDto(usr);
    }

}