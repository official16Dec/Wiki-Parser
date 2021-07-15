package com.sundroid.wikiparser.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeaturedImage {
    @SerializedName("title")
    private String title;

    @SerializedName("pageid")
    private String pageid;

    @SerializedName("imageinfo")
    private List<ImageInfo> imageinfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImageInfo> getImageinfo() {
        return imageinfo;
    }

    public void setImageinfo(List<ImageInfo> imageinfo) {
        this.imageinfo = imageinfo;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }
}
