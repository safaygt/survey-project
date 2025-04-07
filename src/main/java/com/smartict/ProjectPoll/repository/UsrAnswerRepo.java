package com.smartict.ProjectPoll.repository;

import com.smartict.ProjectPoll.entity.Question;
import com.smartict.ProjectPoll.entity.Usr;
import com.smartict.ProjectPoll.entity.UsrAnswer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsrAnswerRepo extends JpaRepository<UsrAnswer, Integer> {

    @Transactional
    void deleteByQuestion(Question question);

    List<UsrAnswer> findByQuestion(Question question, Sort sort);

    boolean existsByUser_IdAndQuestion_Survey_Id(Integer userId, Integer surveyId);

    List<UsrAnswer> findByUser(Usr user);

    List<UsrAnswer> findByQuestion_Survey_Id(Integer surveyId); // Bu satırı ekleyin
}