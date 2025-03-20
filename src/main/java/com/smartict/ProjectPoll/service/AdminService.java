package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.entity.Roles;
import com.smartict.ProjectPoll.entity.Survey;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.entity.UsrAnswer;
import com.smartict.ProjectPoll.mapper.UserMapper;
import com.smartict.ProjectPoll.repository.RolesRepo;
import com.smartict.ProjectPoll.repository.SurveyRepo;
import com.smartict.ProjectPoll.repository.UserRepo;
import com.smartict.ProjectPoll.repository.UsrAnswerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;


    @Autowired
    private UsrAnswerRepo usrAnswerRepo;


    @Autowired
    private RolesRepo rolesRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SurveyRepo surveyRepo;

    @Autowired
    private SurveyService surveyService; // SurveyService'i enjekte et

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(String username) {
        Usr usr = userRepo.findByUsername(username);
        if (usr == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepo.delete(usr);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDTO updateUser(String username, UserDTO userDTO) {
        Usr usr = userRepo.findByUsername(username);
        if (usr == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userMapper.updateEntityFromDto(userDTO, usr);

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            usr.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userMapper.toDto(userRepo.save(usr));
    }

    public UserDTO getUserByUsername(String username) {
        Usr usr = userRepo.findByUsername(username);
        if (usr == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userMapper.toDto(usr);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usr usr = userRepo.findByUsername(username);

        if (usr == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Kullanıcı adını güncelle
        usr.setUsername(userDTO.getUsername());

        // Yeni şifre varsa, bcrypt'le ve güncelle
        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
            usr.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        return userMapper.toDto(userRepo.save(usr));
    }

    @Transactional
    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usr usr = userRepo.findByUsername(username);

        if (usr == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Kullanıcının anket cevaplarını sil
        List<UsrAnswer> usrAnswers = usrAnswerRepo.findByUser(usr);
        usrAnswerRepo.deleteAll(usrAnswers);

        // Kullanıcının anketlerini sil (eğer varsa)
        List<Survey> surveys = surveyRepo.findByFKuserID(usr, Sort.by(Sort.Direction.ASC, "id"));
        for (Survey survey : surveys) {
            surveyService.deleteSurvey(survey.getId());
        }

        userRepo.delete(usr);
    }
}