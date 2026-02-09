package com.ahsan.hibernates.learning_management_system.Repository;

import  com.ahsan.hibernates.learning_management_system.Entity.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, String> {

    @Query("SELECT s FROM Subtopic s WHERE s.topic.course.id = :courseId")
    List<Subtopic> findAllByCourseId(@Param("courseId") String courseId);
}