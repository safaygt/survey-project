package com.smartict.ProjectPoll.dto;

import com.smartict.ProjectPoll.entity.EnumRole;
import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String name;
    private String lastName;
    private String username;
    private String password;
    private EnumRole role;
}