package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.AnswerOptionDTO;
import com.smartict.ProjectPoll.entity.AnswerOption;
import com.smartict.ProjectPoll.entity.Question;
import com.smartict.ProjectPoll.mapper.AnswerOptionMapper;
import com.smartict.ProjectPoll.repository.AnswerOptionRepo;
import com.smartict.ProjectPoll.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerOptionService {

    @Autowired
    private AnswerOptionRepo answerOptionRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private AnswerOptionMapper answerOptionMapper;

    public AnswerOptionDTO updateAnswerOption(Integer answerOptionId, AnswerOptionDTO answerOptionDTO) {
        AnswerOption answerOption = answerOptionRepo.findById(answerOptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer option not found"));

        answerOption.setOptionText(answerOptionDTO.getOptionText());
        return answerOptionMapper.toDto(answerOptionRepo.save(answerOption));
    }

    public void deleteAnswerOption(Integer answerOptionId) {
        AnswerOption answerOption = answerOptionRepo.findById(answerOptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer option not found"));
        answerOptionRepo.delete(answerOption);
    }

    public AnswerOptionDTO createAnswerOption(AnswerOptionDTO answerOptionDTO) {
        AnswerOption answerOption = answerOptionMapper.toEntity(answerOptionDTO);
        Question question = questionRepo.findById(answerOptionDTO.getQuestionId()).orElse(null);
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found with id: " + answerOptionDTO.getQuestionId());
        }
        answerOption.setQuestion(question);
        return answerOptionMapper.toDto(answerOptionRepo.save(answerOption));
    }

    public List<AnswerOptionDTO> getAnswerOptionsByQuestionId(Integer questionId, Sort sort) { // Sort parametresini ekleyin
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        List<AnswerOption> answerOptions = answerOptionRepo.findByQuestion(question, sort); // Sort nesnesini kullanın
        return answerOptions.stream()
                .map(answerOptionMapper::toDto)
                .collect(Collectors.toList());
    }
}