package com.sundroid.wikiparser.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QueryResult {

    @SerializedName("allcategories")
    private List<CategoryModel> allcategories;

    public List<CategoryModel> getAllcategories() {
        return allcategories;
    }

    public void setAllcategories(List<CategoryModel> allcategories) {
        this.allcategories = allcategories;
    }
}
