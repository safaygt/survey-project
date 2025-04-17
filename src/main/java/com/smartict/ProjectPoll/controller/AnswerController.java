package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.dto.UsrAnswerDTO;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.jwt.JwtUtil;
import com.smartict.ProjectPoll.service.AdminService;
import com.smartict.ProjectPoll.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("v1/surveys/{surveyId}/user-answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final JwtUtil jwtUtil;
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Void> createUsrAnswer(@PathVariable Integer surveyId, @RequestBody List<UsrAnswerDTO> usrAnswerDTOs) {
        try {
            answerService.createUsrAnswer(surveyId, usrAnswerDTOs);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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
    public ResponseEntity<Boolean> checkSurveyAnswered(@PathVariable Integer surveyId) {
        boolean answered = answerService.checkSurveyAnswered(surveyId);
        return ResponseEntity.ok(answered);
    }

    @GetMapping("/answered-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAnsweredUsers(@PathVariable Integer surveyId) {
        try {
            List<Usr> users = answerService.getUsersAnsweredSurvey(surveyId);
            return ResponseEntity.ok(users.stream()
                    .map(adminService::getUserDTOFromUsr)
                    .toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/{username}/answers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UsrAnswerDTO>> getUserSurveyAnswersByUsername(@PathVariable Integer surveyId, @PathVariable String username) {
        try {
            List<UsrAnswerDTO> answers = answerService.getAnswerByUsername(username).stream()
                    .filter(answer -> answer.getQuestionId() != null && answer.getQuestionId() != 0)
                    .toList();
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}