package com.ahsan.hibernates.learning_management_system.Controller;

import com.ahsan.hibernates.learning_management_system.DTO.Response.ProgressResponse;
import com.ahsan.hibernates.learning_management_system.DTO.Response.SubtopicCompleteResponse;
import com.ahsan.hibernates.learning_management_system.Service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Tag(name = "Progress", description = "Track and manage course progress")
@SecurityRequirement(name = "bearerAuth")
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/subtopics/{subtopicId}/complete")
    @Operation(
            summary = "Mark subtopic as completed",
            description = "Mark a specific subtopic as completed for the authenticated user. This is idempotent - calling it multiple times has the same effect."
    )
    public ResponseEntity<SubtopicCompleteResponse> markSubtopicComplete(
            @PathVariable String subtopicId,
            Authentication authentication
    ) {
        SubtopicCompleteResponse response = progressService.markSubtopicComplete(subtopicId, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/enrollments/{enrollmentId}")
    @Operation(
            summary = "Get enrollment progress",
            description = "Retrieve detailed progress information for a specific enrollment, including completion percentage and list of completed subtopics"
    )
    public ResponseEntity<ProgressResponse> getEnrollmentProgress(
            @PathVariable Long enrollmentId,
            Authentication authentication
    ) {
        ProgressResponse response = progressService.getEnrollmentProgress(enrollmentId, authentication);
        return ResponseEntity.ok(response);
    }
}
