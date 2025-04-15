package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.SurveyDTO;
import com.smartict.ProjectPoll.dto.UsrAnswerDTO;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.jwt.JwtUtil;
import com.smartict.ProjectPoll.service.AdminService;
import com.smartict.ProjectPoll.service.AnswerService;
import com.smartict.ProjectPoll.service.SurveyService;
import com.smartict.ProjectPoll.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/surveys/{surveyId}/user-answer")
@CrossOrigin
public class AnswerController {

    private final AnswerService answerService;
    private final SurveyService surveyService;
    private final JwtUtil jwtUtil;
    private final AdminService adminService;

    @Autowired
    public AnswerController(AnswerService answerService, JwtUtil jwtUtil, SurveyService surveyService, AdminService adminService) {
        this.answerService = answerService;
        this.jwtUtil = jwtUtil;
        this.surveyService = surveyService;
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<?> createUsrAnswer(@PathVariable Integer surveyId, @RequestBody List<UsrAnswerDTO> usrAnswerDTOs, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
            }

            String token = authorizationHeader.substring(7);

            if (jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired.");
            }

            Integer userId = jwtUtil.extractUserId(token);
            answerService.createUsrAnswer(surveyId, usrAnswerDTOs, userId);
            return ResponseEntity.ok("Answers submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user answer: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UsrAnswerDTO>> getAnswers() {
        try {
            List<UsrAnswerDTO> answers = answerService.getAllAnswers(Sort.by(Sort.Direction.ASC, "id"));
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/check-answered")
    public ResponseEntity<?> checkSurveyAnswered(@PathVariable Integer surveyId, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
            }

            String token = authorizationHeader.substring(7);

            if (jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired.");
            }

            Integer userId = jwtUtil.extractUserId(token);
            boolean answered = answerService.checkSurveyAnswered(surveyId, userId);
            return ResponseEntity.ok(answered);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking survey answer: " + e.getMessage());
        }
    }

    @GetMapping("/answered-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAnsweredUsers(@PathVariable Integer surveyId) {
        try {
            List<Usr> users = answerService.getUsersAnsweredSurvey(surveyId);
            return ResponseEntity.ok(users.stream()
                    .map(usr -> adminService.getUserDTOFromUsr(usr)) // UserService'i kullanarak Usr'dan UserDTO'ya dönüşüm
                    .collect(Collectors.toList()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting answered users: " + e.getMessage());
        }
    }

    //Kullanıcının ilgili ankete ait cevapları önüme gelecek. Buradaki işlem daha bitmedi
    @GetMapping("/{username}/answers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserSurveyAnswersByUsername(@PathVariable Integer surveyId, @PathVariable String username) {
        try {
            List<UsrAnswerDTO> answers = answerService.getAnswerByUsername(username, Sort.by(Sort.Direction.ASC, "id")).stream()
                    .filter(answer -> answer.getQuestionId() != null && answer.getQuestionId() != 0 && answer.getQuestionId() != null)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(answers);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting user survey answers: " + e.getMessage());
        }
    }
}