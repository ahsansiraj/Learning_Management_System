package com.ahsan.hibernates.learning_management_system.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseDetailResponse {
    private String id;
    private String title;
    private String description;
    private List<TopicDto> topics;

    @Data
    @Builder
    public static class TopicDto {
        private String id;
        private String title;
        private List<SubtopicDto> subtopics;
    }

    @Data
    @Builder
    public static class SubtopicDto {
        private String id;
        private String title;
        private String content;
    }
}