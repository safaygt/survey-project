package com.smartict.ProjectPoll.service;

import com.smartict.ProjectPoll.entity.EnumRole;
import com.smartict.ProjectPoll.entity.Roles;
import com.smartict.ProjectPoll.repository.RolesRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolesRepo roleRepository;

    public String getRoleTextById(String roleText) {
        return roleRepository.findByRoleText(EnumRole.valueOf(roleText)).map(Roles::getRoleText).map(EnumRole::name).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }
}