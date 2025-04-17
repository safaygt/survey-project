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
            ResponseEntity.ok(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping
    public ResponseEntity<Void> createSurvey(@Valid @RequestBody SurveyDTO surveyDTO) {
        try {
            surveyService.createSurvey(surveyDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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
    public ResponseEntity<Void> updateSurvey(
            @PathVariable Integer surveyId,
            @RequestBody SurveyDTO surveyDTO) {
        try {
            surveyService.updateSurvey(surveyId, surveyDTO);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody QuestionDTO questionDTO) {
        try {
            questionService.createQuestion(questionDTO);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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
    public ResponseEntity<Void> updateQuestion(
            @PathVariable Integer questionId,
            @RequestBody QuestionDTO questionDTO) {
        try {


            questionService.updateQuestion(questionId, questionDTO);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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

    @PostMapping("/answer-options")
    public ResponseEntity<Void> createAnswerOption(@RequestBody AnswerOptionDTO answerOptionDTO) {
        try {
            answerOptionService.createAnswerOption(answerOptionDTO);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/answer-options/{answerOptionId}")
    public ResponseEntity<Void> updateAnswerOption(
            @PathVariable Integer answerOptionId,
            @RequestBody AnswerOptionDTO answerOptionDTO) {
        try {
            answerOptionService.updateAnswerOption(answerOptionId, answerOptionDTO);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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