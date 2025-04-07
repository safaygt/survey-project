package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.SurveyDTO;
import com.smartict.ProjectPoll.entity.*;
import com.smartict.ProjectPoll.mapper.SurveyMapper;
import com.smartict.ProjectPoll.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepo surveyRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private AnswerOptionRepo answerOptionRepo;

    @Autowired
    private UsrAnswerRepo usrAnswerRepo;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SurveyDTO> getSurveysByUserId(Integer userId, Sort sort) {
        Usr user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Survey> surveys = surveyRepo.findByFKuserID(user, sort);
        return surveys.stream()
                .map(surveyMapper::toDto)
                .collect(Collectors.toList());
    }

    public SurveyDTO updateSurvey(Integer surveyId, SurveyDTO surveyDTO) {
        Survey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found"));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usr user = userRepo.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Survey updatedSurvey = surveyMapper.toEntity(surveyDTO);
        updatedSurvey.setId(surveyId);

        survey.setSurveyName(surveyDTO.getSurveyName());
        surveyRepo.save(survey);

        return surveyMapper.toDto(survey);
    }

    @Transactional
    public void deleteSurvey(Integer surveyId) {
        Survey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found"));

        List<Question> questions = questionRepo.findBySurvey(survey, Sort.by(Sort.Direction.ASC, "id"));
        for (Question question : questions) {
            List<AnswerOption> answerOptions = answerOptionRepo.findByQuestion(question, Sort.by(Sort.Direction.ASC, "id"));
            List<UsrAnswer> usrAnswers = usrAnswerRepo.findByQuestion(question, Sort.by(Sort.Direction.ASC, "id"));
            usrAnswerRepo.deleteAll(usrAnswers);
            answerOptionRepo.deleteAll(answerOptions);
            questionRepo.delete(question);
        }

        surveyRepo.delete(survey);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SurveyDTO> getAllSurveys(Sort sort) {
        return surveyRepo.findAll(sort).stream()
                .map(surveyMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SurveyDTO createSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyMapper.toEntity(surveyDTO);
        Usr user = userRepo.findById(surveyDTO.getFkuserID()).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + surveyDTO.getFkuserID());
        }
        survey.setFKuserID(user);
        return surveyMapper.toDto(surveyRepo.save(survey));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SurveyDTO getSurveyById(Integer surveyId) {
        Survey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found"));
        return surveyMapper.toDto(survey);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SurveyDTO> getAnsweredSurveysByUserId(Integer userId, Sort sort) {
        Usr user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Integer> answeredSurveyIds = usrAnswerRepo.findByUser(user).stream()
                .map(usrAnswer -> usrAnswer.getQuestion().getSurvey().getId())
                .distinct()
                .collect(Collectors.toList());

        List<Survey> surveys = surveyRepo.findAllById(answeredSurveyIds);
        return surveys.stream()
                .map(surveyMapper::toDto)
                .collect(Collectors.toList());
    }
}