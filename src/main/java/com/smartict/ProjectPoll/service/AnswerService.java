package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.UsrAnswerDTO;
import com.smartict.ProjectPoll.entity.*;
import com.smartict.ProjectPoll.mapper.UsrAnswerMapper;
import com.smartict.ProjectPoll.repository.AnswerOptionRepo;
import com.smartict.ProjectPoll.repository.QuestionRepo;
import com.smartict.ProjectPoll.repository.UsrAnswerRepo;
import com.smartict.ProjectPoll.repository.UserRepository;
import java.util.List;
import java.util.Objects;

import com.smartict.ProjectPoll.util.UserHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final UsrAnswerRepo usrAnswerRepo;
    private final UsrAnswerMapper usrAnswerMapper;
    private final UserRepository userRepository;
    private final QuestionRepo questionRepo;
    private final AnswerOptionRepo answerOptionRepo;



    public void createUsrAnswer(Integer surveyId, List<UsrAnswerDTO> usrAnswerDTOs, Integer userId) {
        Usr usr = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        for (UsrAnswerDTO usrAnswerDTO : usrAnswerDTOs) {
            Question question = questionRepo.findById(usrAnswerDTO.getQuestionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));

            if (!question.getSurvey().getId().equals(surveyId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question does not belong to the given survey.");
            }

            UsrAnswer usrAnswer = usrAnswerMapper.toEntity(usrAnswerDTO);
            usrAnswer.setQuestion(question);
            usrAnswer.setUser(usr);

            if (question.getQuestionType().toString().equals("SHORT_ANSWER")) {
                usrAnswer.setAnswerText(usrAnswerDTO.getAnswerText());
                usrAnswer.setAnswerOption(null);
            } else {
                if (usrAnswerDTO.getAnswerOptionId() != null) {
                    AnswerOption answerOption = answerOptionRepo.findById(usrAnswerDTO.getAnswerOptionId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer option not found"));
                    usrAnswer.setAnswerOption(answerOption);
                    usrAnswer.setAnswerText(answerOption.getOptionText());
                }
            }

            usrAnswerRepo.save(usrAnswer);
        }
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UsrAnswerDTO> getAnswerByUsr (Integer usrId, Sort sort){
        Usr usr = userRepository.findById(usrId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<UsrAnswer> answers = usrAnswerRepo.findByUser(usr);
        return answers.stream().map(usrAnswerMapper::toDto)
                .toList();

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UsrAnswerDTO> getAnswerByUsername(String username) {
        Usr usr = userRepository.findByUsername(username);
        if (usr == null) {
            throw new EntityNotFoundException("User not found");
        }
        List<UsrAnswer> answers = usrAnswerRepo.findByUser(usr);
        return answers.stream().map(usrAnswerMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UsrAnswerDTO> getAllAnswers(Sort sort) {
        return usrAnswerRepo.findAll(sort).stream()
                .map(usrAnswerMapper::toDto)
                .toList();
    }


    public boolean checkSurveyAnswered(Integer surveyId) {
        String currentUserName = UserHelper.getCurrentUserName();
        return usrAnswerRepo.existsByUser_UsernameAndQuestion_Survey_Id(currentUserName, surveyId);
    }

    public List<Usr> getUsersAnsweredSurvey(Integer surveyId) {
        return usrAnswerRepo.findByQuestion_Survey_Id(surveyId).stream()
                .map(UsrAnswer::getUser)
                .distinct()
                .toList();
    }
}