package com.ezen.lolketing.model;

import java.util.Map;

public class BoardDTO {
    private String team;
    private Long timestamp;
    private String userId;
    private String subject;
    private String title;
    private String content;
    private String image;
    private int commentCounts;
    private Map<String, Boolean> like;
    private int likeCounts;
    private int views;

    public BoardDTO() {
    }

    public BoardDTO(String team, Long timestamp, String userId, String subject, String title, String content, String image, int commentCounts, Map<String, Boolean> like, int likeCounts, int views) {
        this.team = team;
        this.timestamp = timestamp;
        this.userId = userId;
        this.subject = subject;
        this.title = title;
        this.content = content;
        this.image = image;
        this.commentCounts = commentCounts;
        this.like = like;
        this.likeCounts = likeCounts;
        this.views = views;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public Map<String, Boolean> getLike() {
        return like;
    }

    public void setLike(Map<String, Boolean> like) {
        this.like = like;
    }

    public int getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(int likeCounts) {
        this.likeCounts = likeCounts;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public static class commentDTO {
        private String userId;
        private Long timestamp;
        private String comment;

        public commentDTO() {
        }

        public commentDTO(String userId, Long timestamp, String comment) {
            this.userId = userId;
            this.timestamp = timestamp;
            this.comment = comment;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
