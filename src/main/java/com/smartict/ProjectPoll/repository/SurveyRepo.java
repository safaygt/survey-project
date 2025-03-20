package com.smartict.ProjectPoll.repository;

import com.smartict.ProjectPoll.entity.Survey;
import com.smartict.ProjectPoll.entity.Usr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort; // Sort sınıfını içe aktarın

import java.util.List;

public interface SurveyRepo extends JpaRepository<Survey, Integer> {
    List<Survey> findByFKuserID(Usr usr, Sort sort); // Sort parametresini ekleyin
}