package com.smartict.ProjectPoll.repository;

import com.smartict.ProjectPoll.entity.AnswerOption;
import com.smartict.ProjectPoll.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnswerOptionRepo extends JpaRepository<AnswerOption, Integer> {
    @Transactional
    void deleteByQuestion(Question question);
    List<AnswerOption> findByQuestionId(Integer questionId, Sort sort);
    List<AnswerOption> findByQuestion(Question question, Sort sort); // Sort parametresini ekleyin
}