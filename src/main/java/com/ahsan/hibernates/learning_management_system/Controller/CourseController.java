package com.ahsan.hibernates.learning_management_system.Controller;

import com.ahsan.hibernates.learning_management_system.DTO.Response.CourseDetailResponse;
import com.ahsan.hibernates.learning_management_system.DTO.Response.CourseListResponse;
import com.ahsan.hibernates.learning_management_system.DTO.Response.EnrollmentResponse;
import com.ahsan.hibernates.learning_management_system.Service.CourseService;
import com.ahsan.hibernates.learning_management_system.Service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Browse courses and enroll")
public class CourseController {
    
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    
    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve a list of all available courses (PUBLIC)")
    public ResponseEntity<CourseListResponse> getAllCourses() {
        CourseListResponse response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{courseId}")
    @Operation(summary = "Get course by ID", description = "Retrieve detailed information about a specific course including topics and subtopics (PUBLIC)")
    public ResponseEntity<CourseDetailResponse> getCourseById(@PathVariable String courseId) {
        CourseDetailResponse response = courseService.getCourseById(courseId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{courseId}/enroll")
    @Operation(
            summary = "Enroll in a course", 
            description = "Enroll the authenticated user in a course (REQUIRES JWT)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<EnrollmentResponse> enrollInCourse(
            @PathVariable String courseId,
            Authentication authentication
    ) {
        EnrollmentResponse response = enrollmentService.enrollInCourse(courseId, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}