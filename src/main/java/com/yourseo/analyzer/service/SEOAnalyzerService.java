package com.yourseo.analyzer.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SEOAnalyzerService {

    public int calculateScore(String title, String description) {
        int score = 0;

        int titleLen = title.length();
        if (titleLen >= 40 && titleLen <= 70)      score += 30;
        else if (titleLen >= 20 && titleLen < 40)  score += 15;
        else if (titleLen > 70 && titleLen <= 90)  score += 15;

        if (title.matches(".*\\d+.*"))             score += 10;

        int descLen = description.length();
        if (descLen >= 250)                        score += 30;
        else if (descLen >= 100)                   score += 15;

        if (description.contains("http"))          score += 10;

        String lower = title.toLowerCase();
        if (lower.contains("how to") || lower.contains("tutorial")
                || lower.contains("guide") || lower.contains("tips")) {
            score += 10;
        }

        return Math.min(score, 100);
    }

    public String getTitleFeedback(String title) {
        int len = title.length();
        if (len < 20)  return "Too Short";
        if (len <= 70) return "Good Length";
        if (len <= 90) return "A Bit Long";
        return "Too Long";
    }

    public String getDescriptionFeedback(String description) {
        int len = description.length();
        if (len == 0)    return "Missing";
        if (len < 100)   return "Too Short";
        if (len <= 5000) return "Good Length";
        return "Very Long";
    }

    public List<String> generateSuggestions(String title, String description) {
        List<String> list = new ArrayList<>();

        if (title.length() < 40)
            list.add("📝 Title is short — aim for 40 to 70 characters.");

        if (title.length() > 70)
            list.add("✂️ Title too long — YouTube cuts off titles above 70 characters.");

        if (!title.matches(".*\\d+.*"))
            list.add("🔢 Add a number to your title (e.g. '5 Tips...' or 'Top 10...')");

        // Only suggest "how to" if it looks like an educational/tech video
// Skip this suggestion for songs, movies, trailers
        boolean looksLikeSong = title.toLowerCase().contains("official video")
                || title.toLowerCase().contains("official audio")
                || title.toLowerCase().contains("lyrics")
                || title.toLowerCase().contains("music video")
                || title.toLowerCase().contains("feat")
                || title.toLowerCase().contains("ft.");

        if (!looksLikeSong
                && !title.toLowerCase().contains("how to")
                && !title.toLowerCase().contains("tutorial")) {
            list.add("🔍 Try adding 'How to' or 'Tutorial' in the title for more traffic.");
        }

        if (description.length() < 250)
            list.add("📄 Write a longer description — aim for at least 250 characters.");

        if (!description.contains("http"))
            list.add("🔗 Add links to your website or social media in the description.");

        if (list.isEmpty())
            list.add("✅ Great job! Your video looks well optimized.");

        return list;
    }
}