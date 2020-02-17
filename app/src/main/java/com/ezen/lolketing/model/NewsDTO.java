package com.ezen.lolketing.model;

import java.io.Serializable;

public class NewsDTO implements Serializable {
    private String title;
    private String thumbnail;
    private String info;
    private String url;

    public NewsDTO() {
    }

    public NewsDTO(String title, String thumbnail, String info, String url) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.info = info;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
