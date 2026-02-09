package com.ahsan.hibernates.learning_management_system.Service;

import com.ahsan.hibernates.learning_management_system.DTO.Response.CourseDetailResponse;
import com.ahsan.hibernates.learning_management_system.DTO.Response.CourseListResponse;
import com.ahsan.hibernates.learning_management_system.Exception.ResourceNotFoundException;
import com.ahsan.hibernates.learning_management_system.Entity.Course;
import com.ahsan.hibernates.learning_management_system.Repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseListResponse getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        List<CourseListResponse.CourseSummary> summaries = courses.stream()
                .map(course -> CourseListResponse.CourseSummary.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .topicCount(course.getTopicCount())
                        .subtopicCount(course.getSubtopicCount())
                        .build())
                .collect(Collectors.toList());

        return CourseListResponse.builder()
                .courses(summaries)
                .build();
    }

    public CourseDetailResponse getCourseById(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course with id '" + courseId + "' does not exist"));

        List<CourseDetailResponse.TopicDto> topics = course.getTopics().stream()
                .map(topic -> CourseDetailResponse.TopicDto.builder()
                        .id(topic.getId())
                        .title(topic.getTitle())
                        .subtopics(topic.getSubtopics().stream()
                                .map(subtopic -> CourseDetailResponse.SubtopicDto.builder()
                                        .id(subtopic.getId())
                                        .title(subtopic.getTitle())
                                        .content(subtopic.getContent())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .topics(topics)
                .build();
    }
}