package com.ahsan.hibernates.learning_management_system.Repository;

import com.ahsan.hibernates.learning_management_system.Entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {
}