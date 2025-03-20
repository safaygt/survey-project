package com.smartict.ProjectPoll.dto;

import lombok.Data;

@Data
public class UsrAnswerDTO {
    private Integer questionId;
    private String answerText;
    private Integer answerOptionId;
}