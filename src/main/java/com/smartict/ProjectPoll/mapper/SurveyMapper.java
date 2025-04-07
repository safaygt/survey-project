package com.smartict.ProjectPoll.mapper;

import com.smartict.ProjectPoll.dto.SurveyDTO;
import com.smartict.ProjectPoll.entity.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SurveyMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fkuserID", target = "FKuserID.id")
    @Mapping(source = "creationDate", target = "creationDate")
    Survey toEntity(SurveyDTO dto);

    @Mapping(source = "FKuserID.id", target = "fkuserID")
    @Mapping(source = "creationDate", target = "creationDate")
    SurveyDTO toDto(Survey entity);
}