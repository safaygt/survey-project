package com.smartict.ProjectPoll.dto;

import com.smartict.ProjectPoll.entity.Roles;
import lombok.Data;

@Data
public class UserDTO {

    private String name;
    private String lastName;
    private String username;
    private String password;
    private Integer roleId; // Rol ID'si
    private String newPassword;
}