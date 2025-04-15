package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.mapper.UserMapper;
import com.smartict.ProjectPoll.repository.RolesRepo;
import com.smartict.ProjectPoll.repository.SurveyRepo;
import com.smartict.ProjectPoll.repository.UsrAnswerRepo;
import com.smartict.ProjectPoll.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final UserRepo userRepo;

    private final UsrAnswerRepo usrAnswerRepo;


    private final RolesRepo rolesRepo;

    private final UserMapper userMapper;

    private final SurveyRepo surveyRepo;

    private final SurveyService surveyService;

    public UserDTO getUserDTOFromUsr(Usr user) {
        return userMapper.toDto(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }


    public UserDTO getUserByUsername(String username) {
        Usr usr = userRepo.findByUsername(username);
        if (usr == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userMapper.toDto(usr);
    }

}