package com.smartict.ProjectPoll.controller;

import com.smartict.ProjectPoll.dto.AnswerOptionDTO;
import com.smartict.ProjectPoll.dto.QuestionDTO;
import com.smartict.ProjectPoll.dto.SurveyDTO;
import com.smartict.ProjectPoll.entity.QuestionType;
import com.smartict.ProjectPoll.service.AnswerOptionService;
import com.smartict.ProjectPoll.service.QuestionService;
import com.smartict.ProjectPoll.service.SurveyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/surveys")
@AllArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final AnswerOptionService answerOptionService;

    @GetMapping
    public ResponseEntity<List<SurveyDTO>> getAllSurveys() {
        try {
            List<SurveyDTO> surveys = surveyService.getAllSurveys(Sort.by(Sort.Direction.ASC, "id"));
            return ResponseEntity.ok(surveys);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // Anket Endpointleri
    @PostMapping
    public ResponseEntity<?> createSurvey(@Valid @RequestBody SurveyDTO surveyDTO) {
        try {
            SurveyDTO createdSurveyDTO = surveyService.createSurvey(surveyDTO);
            return ResponseEntity.ok(createdSurveyDTO);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SurveyDTO>> getSurveysByUserId(@PathVariable Integer userId) {
        try {
            List<SurveyDTO> surveys = surveyService.getSurveysByUserId(userId, Sort.by(Sort.Direction.ASC, "id"));
            return ResponseEntity.ok(surveys);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/{surveyId}/{userId}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Integer surveyId, @PathVariable Integer userId) {
        try {
            surveyService.deleteSurvey(surveyId);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Integer surveyId) {
        try {
            SurveyDTO surveyDTO = surveyService.getSurveyById(surveyId);
            return ResponseEntity.ok(surveyDTO);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/{surveyId}")
    public ResponseEntity<?> updateSurvey(
            @PathVariable Integer surveyId,
            @RequestBody SurveyDTO surveyDTO) {
        try {
            SurveyDTO updatedSurvey = surveyService.updateSurvey(surveyId, surveyDTO);
            return ResponseEntity.ok(updatedSurvey);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Soru Endpointleri
    @PostMapping("/questions")
    public ResponseEntity<?> createQuestion(@RequestBody QuestionDTO questionDTO) {
        try {
            QuestionDTO createdQuestionDTO = questionService.createQuestion(questionDTO);
            return ResponseEntity.ok(createdQuestionDTO);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{surveyId}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsBySurveyId(@PathVariable Integer surveyId) {
        try {
            List<QuestionDTO> questionDTOs = questionService.getQuestionsBySurveyId(surveyId, Sort.by(Sort.Direction.ASC, "id"));
            return ResponseEntity.ok(questionDTOs);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PatchMapping("/questions/{questionId}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Integer questionId,
            @RequestBody QuestionDTO questionDTO) {
        try {
            QuestionDTO updatedQuestion = questionService.updateQuestion(questionId, questionDTO);
            return ResponseEntity.ok(updatedQuestion);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer questionId) {
        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Cevap Seçeneği Endpointleri
    @PostMapping("/answer-options")
    public ResponseEntity<?> createAnswerOption(@RequestBody AnswerOptionDTO answerOptionDTO) {
        try {
            AnswerOptionDTO createdAnswerOption = answerOptionService.createAnswerOption(answerOptionDTO);
            return ResponseEntity.ok(createdAnswerOption);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/answer-options/{answerOptionId}")
    public ResponseEntity<?> updateAnswerOption(
            @PathVariable Integer answerOptionId,
            @RequestBody AnswerOptionDTO answerOptionDTO) {
        try {
            AnswerOptionDTO updatedAnswerOption = answerOptionService.updateAnswerOption(answerOptionId, answerOptionDTO);
            return ResponseEntity.ok(updatedAnswerOption);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/answer-options/{answerOptionId}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable Integer answerOptionId) {
        try {
            answerOptionService.deleteAnswerOption(answerOptionId);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<List<AnswerOptionDTO>> getAnswerOptionsByQuestionId(@PathVariable Integer questionId) {
        try {
            List<AnswerOptionDTO> answerOptions = answerOptionService.getAnswerOptionsByQuestionId(questionId, Sort.by(Sort.Direction.ASC, "id"));
            return ResponseEntity.ok(answerOptions);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }


    // Soru Tipleri Endpointi
    @GetMapping("/question-types")
    public ResponseEntity<QuestionType[]> getQuestionTypes() {
        return ResponseEntity.ok(QuestionType.values());
    }
}