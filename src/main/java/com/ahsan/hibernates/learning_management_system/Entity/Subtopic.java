package com.ahsan.hibernates.learning_management_system.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subtopics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subtopic {

    @Id
    private String id; // e.g., "velocity"

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content; // Markdown content

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @OneToMany(mappedBy = "subtopic", cascade = CascadeType.ALL)
    private List<SubtopicProgress> progressList = new ArrayList<>();
}