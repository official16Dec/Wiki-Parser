package com.sundroid.wikiparser.models;

import com.google.gson.annotations.SerializedName;

public class ImageInfo {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
