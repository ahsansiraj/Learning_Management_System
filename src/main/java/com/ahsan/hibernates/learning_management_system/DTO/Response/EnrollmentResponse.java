package com.ahsan.hibernates.learning_management_system.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EnrollmentResponse {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private LocalDateTime enrolledAt;
}