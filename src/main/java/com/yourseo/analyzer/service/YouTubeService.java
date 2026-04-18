package com.yourseo.analyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private RestTemplate restTemplate = new RestTemplate();

    public String extractVideoId(String url) {

        if (url.contains("youtu.be/")) {
            return url.split("youtu.be/")[1].split("\\?")[0].trim();
        }

        if (url.contains("v=")) {
            return url.split("v=")[1].split("&")[0].trim();
        }

        return url.trim();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> fetchVideoData(String videoId) {

        String apiUrl = "https://www.googleapis.com/youtube/v3/videos"
                + "?part=snippet"
                + "&id=" + videoId
                + "&key=" + apiKey;

        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) response.get("items");

            if (!items.isEmpty()) {
                return (Map<String, Object>) items.get(0).get("snippet");
            }
        }

        return Map.of();
    }
}
