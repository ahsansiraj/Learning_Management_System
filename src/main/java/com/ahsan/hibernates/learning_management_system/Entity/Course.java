package com.ahsan.hibernates.learning_management_system.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    private String id; // e.g., "physics-101"

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<Topic> topics = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();

    public int getTopicCount() {
        return topics.size();
    }

    public int getSubtopicCount() {
        return topics.stream()
                .mapToInt(topic -> topic.getSubtopics().size())
                .sum();
    }
}