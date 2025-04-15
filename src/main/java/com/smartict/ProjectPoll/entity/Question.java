    package com.smartict.ProjectPoll.entity;

    import jakarta.persistence.*;
    import lombok.Data;

    @Entity
    @Table(name = "question")
    @Data
    public class Question {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "surveyId", nullable = false)
        private Survey survey;

        @Column(nullable = false)
        private String questionText;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private QuestionType questionType;

        @Column(nullable = true)
        private Boolean mandatory;
    }