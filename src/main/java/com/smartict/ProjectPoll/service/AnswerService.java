package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.SurveyDTO;
import com.smartict.ProjectPoll.dto.UsrAnswerDTO;
import com.smartict.ProjectPoll.entity.AnswerOption;
import com.smartict.ProjectPoll.entity.Question;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.entity.UsrAnswer;
import com.smartict.ProjectPoll.mapper.UsrAnswerMapper;
import com.smartict.ProjectPoll.repository.AnswerOptionRepo;
import com.smartict.ProjectPoll.repository.QuestionRepo;
import com.smartict.ProjectPoll.repository.UsrAnswerRepo;
import com.smartict.ProjectPoll.repository.UserRepo;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AnswerService {

    private final UsrAnswerRepo usrAnswerRepo;
    private final UsrAnswerMapper usrAnswerMapper;
    private final UserRepo userRepo;
    private final QuestionRepo questionRepo;
    private final AnswerOptionRepo answerOptionRepo;

    @Autowired
    public AnswerService(UsrAnswerRepo usrAnswerRepo, UsrAnswerMapper usrAnswerMapper, UserRepo userRepo, QuestionRepo questionRepo, AnswerOptionRepo answerOptionRepo) {
        this.usrAnswerRepo = usrAnswerRepo;
        this.usrAnswerMapper = usrAnswerMapper;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.answerOptionRepo = answerOptionRepo;
    }

    public void createUsrAnswer(Integer surveyId, List<UsrAnswerDTO> usrAnswerDTOs, Integer userId) {
        Usr usr = userRepo.findById(userId)
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
        Usr usr = userRepo.findById(usrId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<UsrAnswer> answers = usrAnswerRepo.findByUser(usr);
        return answers.stream().map(usrAnswerMapper::toDto)
                .collect(Collectors.toList());

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UsrAnswerDTO> getAllAnswers(Sort sort) {
        return usrAnswerRepo.findAll(sort).stream()
                .map(usrAnswerMapper::toDto)
                .collect(Collectors.toList());
    }


    public boolean checkSurveyAnswered(Integer surveyId, Integer userId) {
        return usrAnswerRepo.existsByUser_IdAndQuestion_Survey_Id(userId, surveyId);
    }
}