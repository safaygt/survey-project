package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.UsrAnswerDTO;
import com.smartict.ProjectPoll.jwt.JwtUtil;
import com.smartict.ProjectPoll.service.AnswerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("v1/surveys/{surveyId}/user-answer")
@CrossOrigin
public class AnswerController {

    private final AnswerService answerService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AnswerController(AnswerService answerService, JwtUtil jwtUtil) {
        this.answerService = answerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createUsrAnswer(@PathVariable Integer surveyId, @RequestBody List<UsrAnswerDTO> usrAnswerDTOs, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
            }

            String token = authorizationHeader.substring(7);

            // SecurityContextHolder üzerinden UserDetails al
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
            }

            Integer userId = jwtUtil.extractUserId(token);
            answerService.createUsrAnswer(surveyId, usrAnswerDTOs, userId);
            return ResponseEntity.ok("Answers submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user answer: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsrAnswerDTO>> getAnswers(){
        try{
            List<UsrAnswerDTO> answers = answerService.getAllAnswers(Sort.by(Sort.Direction.ASC, "id"));
            return ResponseEntity.ok(answers);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());

        }

    }

    @GetMapping("/specific-answer/{userId}")
    public ResponseEntity<List<UsrAnswerDTO>> getAnswersByUser(@PathVariable Integer userId){
    try {
        List<UsrAnswerDTO> answers = answerService.getAnswerByUsr(userId, Sort.by(Sort.Direction.ASC, "id"));
        return ResponseEntity.ok(answers);
    }catch (ResponseStatusException e){
        return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
    }catch (Exception e){
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

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
            }

            Integer userId = jwtUtil.extractUserId(token);
            boolean answered = answerService.checkSurveyAnswered(surveyId, userId);
            return ResponseEntity.ok(answered);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking survey answer: " + e.getMessage());
        }
    }
}