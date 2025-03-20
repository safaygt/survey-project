package com.smartict.ProjectPoll.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
public class JwtResponse {

    private String token;
    private HttpStatus responseCode;
    private Integer userId;
    private String username;

}
