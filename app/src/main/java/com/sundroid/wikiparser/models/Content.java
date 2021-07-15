package com.sundroid.wikiparser.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Content {
    @SerializedName("title")
    private String title;

    @SerializedName("pageid")
    private String pageid;

    @SerializedName("revisions")
    private List<ContentText> revisions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContentText> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<ContentText> revisions) {
        this.revisions = revisions;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }
}
