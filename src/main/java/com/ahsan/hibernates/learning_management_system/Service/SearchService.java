package com.ahsan.hibernates.learning_management_system.Service;


import com.ahsan.hibernates.learning_management_system.Repository.CourseRepository;
import com.ahsan.hibernates.learning_management_system.Entity.Course;
import com.ahsan.hibernates.learning_management_system.Entity.Subtopic;
import com.ahsan.hibernates.learning_management_system.Entity.Topic;
import com.ahsan.hibernates.learning_management_system.DTO.Response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CourseRepository courseRepository;

    public SearchResponse search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return SearchResponse.builder()
                    .query(query)
                    .results(List.of())
                    .build();
        }

        String searchTerm = query.toLowerCase().trim();
        List<Course> matchingCourses = courseRepository.searchCourses(searchTerm);

        List<SearchResponse.SearchResult> results = matchingCourses.stream()
                .map(course -> buildSearchResult(course, searchTerm))
                .filter(result -> !result.getMatches().isEmpty())
                .collect(Collectors.toList());

        return SearchResponse.builder()
                .query(query)
                .results(results)
                .build();
    }

    private SearchResponse.SearchResult buildSearchResult(Course course, String query) {
        List<SearchResponse.Match> matches = new ArrayList<>();

        // Check course title match
        if (containsIgnoreCase(course.getTitle(), query)) {
            matches.add(SearchResponse.Match.builder()
                    .type("course")
                    .topicTitle(null)
                    .subtopicId(null)
                    .subtopicTitle(null)
                    .snippet(course.getTitle())
                    .build());
        }

        // Check course description match
        if (containsIgnoreCase(course.getDescription(), query)) {
            matches.add(SearchResponse.Match.builder()
                    .type("course")
                    .topicTitle(null)
                    .subtopicId(null)
                    .subtopicTitle(null)
                    .snippet(extractSnippet(course.getDescription(), query))
                    .build());
        }

        // Check topics and subtopics
        for (Topic topic : course.getTopics()) {
            // Topic title match
            if (containsIgnoreCase(topic.getTitle(), query)) {
                matches.add(SearchResponse.Match.builder()
                        .type("topic")
                        .topicTitle(topic.getTitle())
                        .subtopicId(null)
                        .subtopicTitle(null)
                        .snippet(topic.getTitle())
                        .build());
            }

            for (Subtopic subtopic : topic.getSubtopics()) {
                // Subtopic title match
                if (containsIgnoreCase(subtopic.getTitle(), query)) {
                    matches.add(SearchResponse.Match.builder()
                            .type("subtopic")
                            .topicTitle(topic.getTitle())
                            .subtopicId(subtopic.getId())
                            .subtopicTitle(subtopic.getTitle())
                            .snippet(subtopic.getTitle())
                            .build());
                }

                // Subtopic content match
                if (containsIgnoreCase(subtopic.getContent(), query)) {
                    matches.add(SearchResponse.Match.builder()
                            .type("content")
                            .topicTitle(topic.getTitle())
                            .subtopicId(subtopic.getId())
                            .subtopicTitle(subtopic.getTitle())
                            .snippet(extractSnippet(subtopic.getContent(), query))
                            .build());
                }
            }
        }

        return SearchResponse.SearchResult.builder()
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .matches(matches)
                .build();
    }

    private boolean containsIgnoreCase(String text, String query) {
        if (text == null) return false;
        return text.toLowerCase().contains(query);
    }

    private String extractSnippet(String content, String query) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        int maxSnippetLength = 150;
        String lowerContent = content.toLowerCase();
        String lowerQuery = query.toLowerCase();

        int queryIndex = lowerContent.indexOf(lowerQuery);
        if (queryIndex == -1) {
            return content.length() <= maxSnippetLength
                    ? content
                    : content.substring(0, maxSnippetLength) + "...";
        }

        // Calculate start position (try to center the query in the snippet)
        String snippet = getString(content, queryIndex, maxSnippetLength);

        return snippet.trim();
    }

    private static String getString(String content, int queryIndex, int maxSnippetLength) {
        int start = Math.max(0, queryIndex - (maxSnippetLength / 2));
        int end = Math.min(content.length(), start + maxSnippetLength);

        // Adjust start if end is at the content boundary
        if (end == content.length()) {
            start = Math.max(0, end - maxSnippetLength);
        }

        String snippet = content.substring(start, end);

        // Add ellipsis if needed
        if (start > 0) {
            snippet = "..." + snippet;
        }
        if (end < content.length()) {
            snippet = snippet + "...";
        }
        return snippet;
    }
}