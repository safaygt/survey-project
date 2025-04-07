package com.smartict.ProjectPoll.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smartict.ProjectPoll.entity.Usr;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SurveyDTO {


    private Integer id;
    private String surveyName;

    @NotNull(message = "fkuserID cannot be null")
    private Integer fkuserID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

}
