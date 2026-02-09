package com.ahsan.hibernates.learning_management_system.Service;

import com.ahsan.hibernates.learning_management_system.Entity.Course;
import com.ahsan.hibernates.learning_management_system.Entity.Enrollment;
import com.ahsan.hibernates.learning_management_system.Entity.Subtopic;
import com.ahsan.hibernates.learning_management_system.Entity.SubtopicProgress;
import com.ahsan.hibernates.learning_management_system.Entity.User;
import com.ahsan.hibernates.learning_management_system.Exception.NotEnrolledException;
import com.ahsan.hibernates.learning_management_system.Exception.ResourceNotFoundException;
import com.ahsan.hibernates.learning_management_system.DTO.Response.ProgressResponse;
import com.ahsan.hibernates.learning_management_system.DTO.Response.SubtopicCompleteResponse;
import com.ahsan.hibernates.learning_management_system.Repository.EnrollmentRepository;
import com.ahsan.hibernates.learning_management_system.Repository.SubtopicProgressRepository;
import com.ahsan.hibernates.learning_management_system.Repository.SubtopicRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final SubtopicRepository subtopicRepository;
    private final SubtopicProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SubtopicCompleteResponse markSubtopicComplete(String subtopicId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subtopic with id '" + subtopicId + "' does not exist"));

        Course course = subtopic.getTopic().getCourse();

        // Check if user is enrolled in the course
        if (!enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new NotEnrolledException(
                    "You must be enrolled in this course to mark subtopics as complete");
        }

        // Check if already completed (idempotent operation)
        SubtopicProgress progress = progressRepository.findByUserAndSubtopic(user, subtopic)
                .orElseGet(() -> {
                    SubtopicProgress newProgress = SubtopicProgress.builder()
                            .user(user)
                            .subtopic(subtopic)
                            .build();
                    return progressRepository.save(newProgress);
                });

        return SubtopicCompleteResponse.builder()
                .subtopicId(subtopic.getId())
                .completed(progress.isCompleted())
                .completedAt(progress.getCompletedAt())
                .build();
    }

    public ProgressResponse getEnrollmentProgress(Long enrollmentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Enrollment enrollment = enrollmentRepository.findByIdAndUser(enrollmentId, user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment with id '" + enrollmentId + "' does not exist"));

        Course course = enrollment.getCourse();
        int totalSubtopics = course.getSubtopicCount();

        List<SubtopicProgress> completedProgress = progressRepository
                .findByUserAndCourseId(user, course.getId());

        int completedCount = completedProgress.size();
        double percentage = totalSubtopics > 0
                ? Math.round((double) completedCount / totalSubtopics * 10000) / 100.0
                : 0;

        List<ProgressResponse.CompletedItem> completedItems = completedProgress.stream()
                .map(p -> ProgressResponse.CompletedItem.builder()
                        .subtopicId(p.getSubtopic().getId())
                        .subtopicTitle(p.getSubtopic().getTitle())
                        .completedAt(p.getCompletedAt())
                        .build())
                .collect(Collectors.toList());

        return ProgressResponse.builder()
                .enrollmentId(enrollment.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .totalSubtopics(totalSubtopics)
                .completedSubtopics(completedCount)
                .completionPercentage(percentage)
                .completedItems(completedItems)
                .build();
    }
}
