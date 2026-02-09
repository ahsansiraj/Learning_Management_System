package com.ahsan.hibernates.learning_management_system.Repository;

import com.ahsan.hibernates.learning_management_system.Entity.Course;
import com.ahsan.hibernates.learning_management_system.Entity.Enrollment;
import com.ahsan.hibernates.learning_management_system.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    boolean existsByUserAndCourse(User user, Course course);
    List<Enrollment> findByUser(User user);
    Optional<Enrollment> findByIdAndUser(Long id, User user);
}