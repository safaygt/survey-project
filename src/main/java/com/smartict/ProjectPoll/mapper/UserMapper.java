package com.smartict.ProjectPoll.mapper;

import com.smartict.ProjectPoll.dto.UserDTO;
import com.smartict.ProjectPoll.entity.EnumRole;
import com.smartict.ProjectPoll.entity.Roles;
import com.smartict.ProjectPoll.entity.Usr;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleTextToRole")
    Usr toEntity(UserDTO dto);

    @Mapping(target = "role", source = "role.roleText")
    UserDTO toDto(Usr entity);

    @Named("roleTextToRole")
    default Roles roleTextToRole(EnumRole role) {
        if (role == null) {
            return null;
        }
        Roles roleEntity = new Roles();
        roleEntity.setRoleText(role);
        return roleEntity;
    }
}