package com.smartict.ProjectPoll.dto;


import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {

    private Integer id;
    private String questionText;
    private Integer surveyId;
    private String questionType;
    private Boolean mandatory;
    private List<String> options;

}
