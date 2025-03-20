package com.smartict.ProjectPoll.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "answerOption")
@Data
public class AnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;

    @Column(nullable = true)
    private String optionText;
}