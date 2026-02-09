package com.ahsan.hibernates.learning_management_system.Repository;


import com.ahsan.hibernates.learning_management_system.Entity.Subtopic;
import com.ahsan.hibernates.learning_management_system.Entity.SubtopicProgress;
import com.ahsan.hibernates.learning_management_system.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubtopicProgressRepository extends JpaRepository<SubtopicProgress, Long> {
    Optional<SubtopicProgress> findByUserAndSubtopic(User user, Subtopic subtopic);

    @Query("SELECT sp FROM SubtopicProgress sp " +
            "WHERE sp.user = :user AND sp.subtopic.topic.course.id = :courseId")
    List<SubtopicProgress> findByUserAndCourseId(@Param("user") User user,
                                                 @Param("courseId") String courseId);
}