package com.smartict.ProjectPoll.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usrAnswer")
@Data
public class UsrAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Usr user;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;

    @Column(nullable = true)
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "answerOptionId", nullable = true)
    private AnswerOption answerOption;
}