package com.smartict.ProjectPoll.repository;

import com.smartict.ProjectPoll.entity.Question;
import com.smartict.ProjectPoll.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort; // Sort sınıfını içe aktarın

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Integer> {
    List<Question> findBySurvey(Survey survey, Sort sort); // Sort parametresini ekleyin
}