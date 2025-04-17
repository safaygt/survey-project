package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.dto.AnswerOptionDTO;
import com.smartict.ProjectPoll.entity.AnswerOption;
import com.smartict.ProjectPoll.entity.Question;
import com.smartict.ProjectPoll.mapper.AnswerOptionMapper;
import com.smartict.ProjectPoll.repository.AnswerOptionRepo;
import com.smartict.ProjectPoll.repository.QuestionRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerOptionService {


    private final AnswerOptionRepo answerOptionRepo;

    private final QuestionRepo questionRepo;

    private final AnswerOptionMapper answerOptionMapper;

    public AnswerOptionDTO updateAnswerOption(Integer answerOptionId, AnswerOptionDTO answerOptionDTO) {
        AnswerOption answerOption = answerOptionRepo.findById(answerOptionId)
                .orElseThrow(() -> new EntityNotFoundException("Answer option not found"));

        answerOption.setOptionText(answerOptionDTO.getOptionText());
        return answerOptionMapper.toDto(answerOptionRepo.save(answerOption));
    }

    public void deleteAnswerOption(Integer answerOptionId) {
        AnswerOption answerOption = answerOptionRepo.findById(answerOptionId)
                .orElseThrow(() -> new EntityNotFoundException("Answer option not found"));
        answerOptionRepo.delete(answerOption);
    }

    public AnswerOptionDTO createAnswerOption(AnswerOptionDTO answerOptionDTO) {
        AnswerOption answerOption = answerOptionMapper.toEntity(answerOptionDTO);
        Question question = questionRepo.findById(answerOptionDTO.getQuestionId()).orElse(null);
        if (question == null) {
            throw new EntityNotFoundException("Question does not match with id: " + answerOptionDTO.getQuestionId());
        }
        answerOption.setQuestion(question);
        return answerOptionMapper.toDto(answerOptionRepo.save(answerOption));
    }

    public List<AnswerOptionDTO> getAnswerOptionsByQuestionId(Integer questionId, Sort sort) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        List<AnswerOption> answerOptions = answerOptionRepo.findByQuestion(question, sort);
        return answerOptions.stream()
                .map(answerOptionMapper::toDto)
                .toList();
    }
}