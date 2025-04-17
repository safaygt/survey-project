package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.QuestionDTO;
import com.smartict.ProjectPoll.entity.*;
import com.smartict.ProjectPoll.mapper.QuestionMapper;
import com.smartict.ProjectPoll.repository.AnswerOptionRepo;
import com.smartict.ProjectPoll.repository.QuestionRepo;
import com.smartict.ProjectPoll.repository.SurveyRepo;
import com.smartict.ProjectPoll.repository.UsrAnswerRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepo questionRepo;

    private final SurveyRepo surveyRepo;

    private final QuestionMapper questionMapper;

    private final UsrAnswerRepo usrAnswerRepo;

    private final AnswerOptionRepo answerOptionRepo;

    public void updateQuestion(Integer questionId, QuestionDTO questionDTO) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException( "Question not found"));

        if(questionDTO.getQuestionText() != null)
            question.setQuestionText(questionDTO.getQuestionText());

        if(questionDTO.getMandatory() != null)
            question.setMandatory(questionDTO.getMandatory());

        // Mevcut questionType'ı koru eğer questionType güncellenmemişse
        if (questionDTO.getQuestionType() != null && !questionDTO.getQuestionType().isEmpty()) {
            try {
                question.setQuestionType(QuestionType.valueOf(questionDTO.getQuestionType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid question type: " + questionDTO.getQuestionType());
            }
        }

        questionMapper.toDto(questionRepo.save(question));
    }

    @Transactional
    public void deleteQuestion(Integer questionId) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        try {
            List<AnswerOption> answerOptions = answerOptionRepo.findByQuestion(question, Sort.by(Sort.Direction.ASC, "id"));
            answerOptionRepo.deleteAll(answerOptions);

            List<UsrAnswer> usrAnswers = usrAnswerRepo.findByQuestion(question, Sort.by(Sort.Direction.ASC, "id"));
            usrAnswerRepo.deleteAll(usrAnswers);

            questionRepo.delete(question);
        } catch (Exception e) {
            logger.error("Error deleting question and related data", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete question and related data");
        }
    }

    public void createQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.toEntity(questionDTO);
        Survey survey = surveyRepo.findById(questionDTO.getSurveyId()).orElse(null);
        if (survey == null) {
            throw new EntityNotFoundException("Survey does not match with id: " + questionDTO.getSurveyId());
        }
        question.setSurvey(survey);

        if (questionDTO.getQuestionType() != null && !questionDTO.getQuestionType().isEmpty()) {
            try {
                question.setQuestionType(QuestionType.valueOf(questionDTO.getQuestionType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid question type: " + questionDTO.getQuestionType());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question type cannot be null or empty.");
        }

        Question savedQuestion = questionRepo.save(question);

        if (questionDTO.getOptions() != null && !questionDTO.getOptions().isEmpty()) {
            for (String optionText : questionDTO.getOptions()) {
                AnswerOption answerOption = new AnswerOption();
                answerOption.setQuestion(savedQuestion);
                answerOption.setOptionText(optionText);
                answerOptionRepo.save(answerOption);
            }
        }

        questionMapper.toDto(savedQuestion);
    }

    public List<QuestionDTO> getQuestionsBySurveyId(Integer surveyId, Sort sort) {
        Survey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));
        List<Question> questions = questionRepo.findBySurvey(survey, sort);

        return questions.stream().map(question -> {
            QuestionDTO dto = questionMapper.toDto(question);
            if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE || question.getQuestionType() == QuestionType.CHECKBOX) {
                List<AnswerOption> answerOptions = answerOptionRepo.findByQuestion(question, Sort.by(Sort.Direction.ASC, "id")); // Sort nesnesini kullanın
                List<String> options = answerOptions.stream()
                        .map(AnswerOption::getOptionText)
                        .toList();
                dto.setOptions(options);
            }
            return dto;
        }).toList();
    }
}