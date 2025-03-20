package com.smartict.ProjectPoll.dto;

import lombok.Data;

@Data
public class AnswerOptionDTO {
    private Integer id;
    private String optionText;
    private Integer questionId;
}