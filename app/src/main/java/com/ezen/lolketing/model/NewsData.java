package com.ezen.lolketing.model;

import java.io.Serializable;

public class NewsData implements Serializable {
    private String title;
    private String urlToImage;
    private String author;
    private String publishedAt;
    private String url;

    public NewsData() {
    }

    public NewsData(String title, String urlToImage, String author, String publishedAt, String url) {
        this.title = title;
        this.urlToImage = urlToImage;
        this.author = author;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
