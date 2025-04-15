package com.smartict.ProjectPoll.mapper;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.entity.Roles;
import com.smartict.ProjectPoll.entity.Usr;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "roleId", qualifiedByName = "roleIdToRole")
    Usr toEntity(UserDTO dto);

    @Mapping(target = "roleId", source = "role.id")
    UserDTO toDto(Usr entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "roleId", qualifiedByName = "roleIdToRole")
    void updateEntityFromDto(UserDTO dto, @MappingTarget Usr entity);

    @Named("roleIdToRole")
    default Roles roleIdToRole(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        Roles role = new Roles();
        role.setId(roleId);
        return role;
    }
}