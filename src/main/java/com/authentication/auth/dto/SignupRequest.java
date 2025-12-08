package com.authentication.auth.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
}