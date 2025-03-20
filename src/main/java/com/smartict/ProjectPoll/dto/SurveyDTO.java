package com.smartict.ProjectPoll.dto;

import com.smartict.ProjectPoll.entity.Usr;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SurveyDTO {


    private Integer id;
    private String surveyName;

    @NotNull(message = "fkuserID cannot be null")
    private Integer fkuserID;

}
