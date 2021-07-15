package com.sundroid.wikiparser.models;

import com.google.gson.annotations.SerializedName;

public class ContentText {
    @SerializedName("*")
    private String contentText;

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}
