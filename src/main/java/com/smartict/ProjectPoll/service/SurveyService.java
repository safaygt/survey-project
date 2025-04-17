package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.SurveyDTO;
import com.smartict.ProjectPoll.entity.*;
import com.smartict.ProjectPoll.mapper.SurveyMapper;
import com.smartict.ProjectPoll.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepo surveyRepo;
    private final UserRepository userRepository;
    private final SurveyMapper surveyMapper;
    private final QuestionRepo questionRepo;
    private final AnswerOptionRepo answerOptionRepo;
    private final UsrAnswerRepo usrAnswerRepo;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SurveyDTO> getSurveysByUserId(Integer userId, Sort sort) {
        Usr user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Survey> surveys = surveyRepo.findByFKuserID(user, sort);
        return surveys.stream()
                .map(surveyMapper::toDto)
                .toList();
    }

    public void updateSurvey(Integer surveyId, SurveyDTO surveyDTO) {
        Survey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));


        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usr user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new EntityNotFoundException("User not found");

        }

        Survey updatedSurvey = surveyMapper.toEntity(surveyDTO);
        updatedSurvey.setId(surveyId);

        survey.setSurveyName(surveyDTO.getSurveyName());
        surveyRepo.save(survey);

        surveyMapper.toDto(survey);
    }

    @Transactional
    public void deleteSurvey(Integer surveyId) {
        Survey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));


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

    public List<SurveyDTO> getAllSurveys(Sort sort) {
        return surveyRepo.findAll(sort).stream()
                .map(surveyMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyMapper.toEntity(surveyDTO);
        Usr user = userRepository.findById(surveyDTO.getFkuserID()).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User does not match with id: "+ surveyDTO.getFkuserID());

        }
        survey.setFKuserID(user);
        surveyMapper.toDto(surveyRepo.save(survey));
    }


    public SurveyDTO getSurveyById(Integer surveyId) {
        Survey survey = surveyRepo.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found"));

        return surveyMapper.toDto(survey);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SurveyDTO> getAnsweredSurveysByUserId(Integer userId, Sort sort) {
        Usr user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        List<Integer> answeredSurveyIds = usrAnswerRepo.findByUser(user).stream()
                .map(usrAnswer -> usrAnswer.getQuestion().getSurvey().getId())
                .distinct()
                .toList();

        List<Survey> surveys = surveyRepo.findAllById(answeredSurveyIds);
        return surveys.stream()
                .map(surveyMapper::toDto)
                .toList();
    }
}