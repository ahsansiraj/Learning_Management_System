package com.ahsan.hibernates.learning_management_system.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseListResponse {
    private List<CourseSummary> courses;

    @Data
    @Builder
    public static class CourseSummary {
        private String id;
        private String title;
        private String description;
        private int topicCount;
        private int subtopicCount;
    }
}