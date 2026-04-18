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
            
            String videoId = youTubeService.extractVideoId(url);

            Map<String, Object> snippet = youTubeService.fetchVideoData(videoId);

            if (snippet.isEmpty()) {
                result.setTitle("Video not found. Check URL or API key.");
                return ResponseEntity.ok(result);
            }

            String title = (String) snippet.getOrDefault("title", "No title");
            String description = (String) snippet.getOrDefault("description", "");
            result.setTitle(title);
            result.setDescription(description);

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

            result.setSeoScore(seoAnalyzerService.calculateScore(title, description));
            result.setTitleFeedback(seoAnalyzerService.getTitleFeedback(title));
            result.setDescriptionFeedback(seoAnalyzerService.getDescriptionFeedback(description));
            result.setSuggestions(seoAnalyzerService.generateSuggestions(title, description));

            result.setTags(tagGeneratorService.generateTags(title, description));

        } catch (Exception e) {
            result.setTitle("Error: " + e.getMessage());
        }

        return ResponseEntity.ok(result);
    }
}
