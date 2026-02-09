package com.ahsan.hibernates.learning_management_system.Controller;

import com.ahsan.hibernates.learning_management_system.DTO.Response.EnrollmentResponse;
import com.ahsan.hibernates.learning_management_system.DTO.Response.ProgressResponse;
import com.ahsan.hibernates.learning_management_system.Service.EnrollmentService;
import com.ahsan.hibernates.learning_management_system.Service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Manage user enrollments and view progress")
@SecurityRequirement(name = "bearerAuth")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    private final ProgressService progressService;
    
    @GetMapping
    @Operation(summary = "Get user enrollments", description = "Retrieve all enrollments for the authenticated user")
    public ResponseEntity<List<EnrollmentResponse>> getUserEnrollments(Authentication authentication) {
        List<EnrollmentResponse> response = enrollmentService.getUserEnrollments(authentication);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{enrollmentId}/progress")
    @Operation(summary = "Get enrollment progress", description = "View progress for a specific enrollment")
    public ResponseEntity<ProgressResponse> getEnrollmentProgress(
            @PathVariable Long enrollmentId,
            Authentication authentication
    ) {
        ProgressResponse response = progressService.getEnrollmentProgress(enrollmentId, authentication);
        return ResponseEntity.ok(response);
    }
}