package com.yourseo.analyzer.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagGeneratorService {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "and", "for", "are", "but", "not", "you", "all",
            "can", "was", "one", "has", "had", "how", "its", "let",
            "may", "now", "own", "see", "she", "than", "that", "then",
            "them", "they", "this", "with", "your", "will", "just",
            "have", "from", "been", "into", "more", "also", "what",
            "when", "who", "why", "use", "about", "would", "which"
    ));

    public List<String> generateTags(String title, String description) {

        String combined = title + " " +
                description.substring(0, Math.min(500, description.length()));

        String[] words = combined
                .replaceAll("[^a-zA-Z0-9 ]", " ")
                .toLowerCase()
                .split("\\s+");

        List<String> tags = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (String word : words) {
            if (word.length() >= 4 && !STOP_WORDS.contains(word) && !seen.contains(word)) {
                tags.add(word);
                seen.add(word);
            }
            if (tags.size() >= 20) break;
        }

        tags.add(0, title.toLowerCase());
        return tags;
    }
}