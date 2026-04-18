package com.yourseo.analyzer.model;

import java.util.List;

public class VideoResponse {

    private String title;
    private String description;
    private String thumbnailUrl;
    private int seoScore;
    private String titleFeedback;
    private String descriptionFeedback;
    private List<String> tags;
    private List<String> suggestions;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public int getSeoScore() { return seoScore; }
    public void setSeoScore(int seoScore) { this.seoScore = seoScore; }

    public String getTitleFeedback() { return titleFeedback; }
    public void setTitleFeedback(String titleFeedback) { this.titleFeedback = titleFeedback; }

    public String getDescriptionFeedback() { return descriptionFeedback; }
    public void setDescriptionFeedback(String descriptionFeedback) { this.descriptionFeedback = descriptionFeedback; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
}