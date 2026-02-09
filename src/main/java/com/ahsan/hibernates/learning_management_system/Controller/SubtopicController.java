package com.ahsan.hibernates.learning_management_system.Controller;

import com.ahsan.hibernates.learning_management_system.DTO.Response.SubtopicCompleteResponse;
import com.ahsan.hibernates.learning_management_system.Service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subtopics")
@RequiredArgsConstructor
@Tag(name = "Subtopics", description = "Mark subtopics as completed")
@SecurityRequirement(name = "bearerAuth")
public class SubtopicController {
    
    private final ProgressService progressService;
    
    @PostMapping("/{subtopicId}/complete")
    @Operation(summary = "Mark subtopic as completed", description = "Mark a subtopic as completed for the authenticated user")
    public ResponseEntity<SubtopicCompleteResponse> markSubtopicComplete(
            @PathVariable String subtopicId,
            Authentication authentication
    ) {
        SubtopicCompleteResponse response = progressService.markSubtopicComplete(subtopicId, authentication);
        return ResponseEntity.ok(response);
    }
}