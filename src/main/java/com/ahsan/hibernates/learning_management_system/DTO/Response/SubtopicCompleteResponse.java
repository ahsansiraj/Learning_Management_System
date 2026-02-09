package com.ahsan.hibernates.learning_management_system.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubtopicCompleteResponse {
    private String subtopicId;
    private boolean completed;
    private LocalDateTime completedAt;
}
