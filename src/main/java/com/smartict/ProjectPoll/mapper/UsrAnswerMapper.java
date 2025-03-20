package com.smartict.ProjectPoll.mapper;

import com.smartict.ProjectPoll.dto.UsrAnswerDTO;
import com.smartict.ProjectPoll.entity.UsrAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsrAnswerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // user alanını ignore et
    @Mapping(source = "questionId", target = "question.id")
    @Mapping(source = "answerOptionId", target = "answerOption.id")
    UsrAnswer toEntity(UsrAnswerDTO dto);

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "answerOption.id", target = "answerOptionId")
    UsrAnswerDTO toDto(UsrAnswer entity);
}