package com.smartict.ProjectPoll.config;

import com.smartict.ProjectPoll.jwt.JwtUtil;
import com.smartict.ProjectPoll.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JwtFilterConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userService, JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(userService, jwtUtil);
    }
}