package com.smartict.ProjectPoll.repository;

import com.smartict.ProjectPoll.entity.EnumRole;
import com.smartict.ProjectPoll.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepo extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByRoleText(EnumRole enumRole);
}