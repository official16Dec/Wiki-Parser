package com.sundroid.wikiparser.models;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("category")
    private String categoryTitle;

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

}
