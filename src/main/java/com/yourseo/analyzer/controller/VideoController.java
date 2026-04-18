package com.yourseo.analyzer.controller;

import com.yourseo.analyzer.model.VideoResponse;
import com.yourseo.analyzer.service.SEOAnalyzerService;
import com.yourseo.analyzer.service.TagGeneratorService;
import com.yourseo.analyzer.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VideoController {

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private SEOAnalyzerService seoAnalyzerService;

    @Autowired
    private TagGeneratorService tagGeneratorService;

    @PostMapping("/analyze")
    public ResponseEntity<VideoResponse> analyzeVideo(@RequestBody Map<String, String> request) {

        String url = request.get("url");
        VideoResponse result = new VideoResponse();

        try {
            // 1. Extract video ID from URL
            String videoId = youTubeService.extractVideoId(url);

            // 2. Call YouTube API
            Map<String, Object> snippet = youTubeService.fetchVideoData(videoId);

            if (snippet.isEmpty()) {
                result.setTitle("Video not found. Check URL or API key.");
                return ResponseEntity.ok(result);
            }

            // 3. Get title and description
            String title = (String) snippet.getOrDefault("title", "No title");
            String description = (String) snippet.getOrDefault("description", "");
            result.setTitle(title);
            result.setDescription(description);

            // 4. Get thumbnail
            @SuppressWarnings("unchecked")
            Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
            if (thumbnails != null) {
                Map<String, Object> thumb =
                        thumbnails.containsKey("maxres") ? (Map<String, Object>) thumbnails.get("maxres") :
                                thumbnails.containsKey("high")   ? (Map<String, Object>) thumbnails.get("high") :
                                        (Map<String, Object>) thumbnails.get("medium");
                if (thumb != null) {
                    result.setThumbnailUrl((String) thumb.get("url"));
                }
            }

            // 5. SEO Analysis
            result.setSeoScore(seoAnalyzerService.calculateScore(title, description));
            result.setTitleFeedback(seoAnalyzerService.getTitleFeedback(title));
            result.setDescriptionFeedback(seoAnalyzerService.getDescriptionFeedback(description));
            result.setSuggestions(seoAnalyzerService.generateSuggestions(title, description));

            // 6. Generate tags
            result.setTags(tagGeneratorService.generateTags(title, description));

        } catch (Exception e) {
            result.setTitle("Error: " + e.getMessage());
        }

        return ResponseEntity.ok(result);
    }
}