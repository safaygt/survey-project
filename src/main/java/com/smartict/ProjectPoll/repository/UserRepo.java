package com.smartict.ProjectPoll.repository;

import com.smartict.ProjectPoll.entity.Usr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Usr, Integer> {

    Usr findByUsername(String username);
}