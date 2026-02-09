package com.ahsan.hibernates.learning_management_system.Service;

import com.ahsan.hibernates.learning_management_system.DTO.Response.EnrollmentResponse;
import com.ahsan.hibernates.learning_management_system.Entity.Course;
import com.ahsan.hibernates.learning_management_system.Entity.Enrollment;
import com.ahsan.hibernates.learning_management_system.Entity.User;
import com.ahsan.hibernates.learning_management_system.Exception.AlreadyEnrolledException;
import com.ahsan.hibernates.learning_management_system.Exception.ResourceNotFoundException;
import com.ahsan.hibernates.learning_management_system.Repository.CourseRepository;
import com.ahsan.hibernates.learning_management_system.Repository.EnrollmentRepository;
import com.ahsan.hibernates.learning_management_system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public EnrollmentResponse enrollInCourse(String courseId, Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        
        // Re-fetch user from database to get a managed entity
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course with id '" + courseId + "' does not exist"));

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new AlreadyEnrolledException("You are already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return EnrollmentResponse.builder()
                .enrollmentId(savedEnrollment.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .enrolledAt(savedEnrollment.getEnrolledAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getUserEnrollments(Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        
        // Re-fetch user from database to get a managed entity
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return enrollmentRepository.findByUser(user).stream()
                .map(enrollment -> EnrollmentResponse.builder()
                        .enrollmentId(enrollment.getId())
                        .courseId(enrollment.getCourse().getId())
                        .courseTitle(enrollment.getCourse().getTitle())
                        .enrolledAt(enrollment.getEnrolledAt())
                        .build())
                .collect(Collectors.toList());
    }
}