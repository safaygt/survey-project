package com.smartict.ProjectPoll.config;

import com.smartict.ProjectPoll.entity.EnumRole;
import com.smartict.ProjectPoll.entity.Roles;
import com.smartict.ProjectPoll.repository.RolesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RolesRepo rolesRepo;

    @Override
    public void run(ApplicationArguments args) {
        for (EnumRole enumRole : EnumRole.values()) {
            if (rolesRepo.findByRoleText(enumRole).isEmpty()) {
                Roles role = new Roles();
                role.setRoleText(enumRole);
                rolesRepo.save(role);
            }
        }
    }
}
