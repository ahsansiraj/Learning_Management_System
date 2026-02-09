package com.ahsan.hibernates.learning_management_system.Seeder;

import com.ahsan.hibernates.learning_management_system.Entity.Course;
import com.ahsan.hibernates.learning_management_system.Entity.Subtopic;
import com.ahsan.hibernates.learning_management_system.Entity.Topic;
import com.ahsan.hibernates.learning_management_system.Repository.CourseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() > 0) {
            log.info("Database already contains data. Skipping seed.");
            return;
        }

        log.info("Seeding database with course data...");

        try {
            InputStream inputStream = new ClassPathResource("seed-data.json").getInputStream();
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode coursesNode = rootNode.get("courses");

            List<Course> courses = new ArrayList<>();

            for (JsonNode courseNode : coursesNode) {
                Course course = new Course();
                course.setId(courseNode.get("id").asText());
                course.setTitle(courseNode.get("title").asText());
                course.setDescription(courseNode.get("description").asText());

                List<Topic> topics = new ArrayList<>();
                JsonNode topicsNode = courseNode.get("topics");

                for (JsonNode topicNode : topicsNode) {
                    Topic topic = new Topic();
                    topic.setId(topicNode.get("id").asText());
                    topic.setTitle(topicNode.get("title").asText());
                    topic.setCourse(course);

                    List<Subtopic> subtopics = new ArrayList<>();
                    JsonNode subtopicsNode = topicNode.get("subtopics");

                    for (JsonNode subtopicNode : subtopicsNode) {
                        Subtopic subtopic = new Subtopic();
                        subtopic.setId(subtopicNode.get("id").asText());
                        subtopic.setTitle(subtopicNode.get("title").asText());
                        subtopic.setContent(subtopicNode.get("content").asText());
                        subtopic.setTopic(topic);

                        subtopics.add(subtopic);
                    }

                    topic.setSubtopics(subtopics);
                    topics.add(topic);
                }

                course.setTopics(topics);
                courses.add(course);
            }

            courseRepository.saveAll(courses);
            log.info("Successfully seeded {} courses", courses.size());

        } catch (Exception e) {
            log.error("Error seeding database", e);
            throw e;
        }
    }
}