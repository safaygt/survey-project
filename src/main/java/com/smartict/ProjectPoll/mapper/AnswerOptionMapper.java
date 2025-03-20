package com.smartict.ProjectPoll.mapper;

import com.smartict.ProjectPoll.dto.AnswerOptionDTO;
import com.smartict.ProjectPoll.entity.AnswerOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerOptionMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "questionId", target = "question.id")
    AnswerOption toEntity(AnswerOptionDTO dto);

    @Mapping(source = "question.id", target = "questionId")
    AnswerOptionDTO toDto(AnswerOption entity);
}