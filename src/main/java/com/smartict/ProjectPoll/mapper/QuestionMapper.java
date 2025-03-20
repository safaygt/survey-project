package com.smartict.ProjectPoll.mapper;

import com.smartict.ProjectPoll.dto.QuestionDTO;
import com.smartict.ProjectPoll.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "surveyId", target = "survey.id")
    //@Mapping(target = "options", ignore = true) //options alanını ignore et
    Question toEntity(QuestionDTO dto);

    @Mapping(source = "survey.id", target = "surveyId")
    @Mapping(target = "options", ignore = true) //options alanını ignore et
    QuestionDTO toDto(Question entity);
}