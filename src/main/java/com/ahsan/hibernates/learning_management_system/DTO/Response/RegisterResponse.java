package com.ahsan.hibernates.learning_management_system.DTO.Response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String email;
    private String message;
}