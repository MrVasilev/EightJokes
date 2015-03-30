package com.neverland.eightjokes.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Vasilev on 15.1.2015 г..
 * <p/>
 * Joke is a entry class.
 */
@ParseClassName("Joke")
public class Joke extends ParseObject {

    public Joke() {
    }

    public String getAuthor() {
        return getString("author_name");
    }

    public void setAuthor(String authorName) {
        if (authorName != null)
            put("author_name", authorName);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        if (content != null)
            put("content", content);
    }

    public int getRateUp() {
        return getInt("rate_up");
    }

    public void incrementRateUp() {
        increment("rate_up");
    }

    public void setRateUp(int value) {
        put("rate_up", value);
    }

    public int getRateDown() {
        return getInt("rate_down");
    }

    public void incrementRateDown() {
        increment("rate_down");
    }

    public void setRateDown(int value) {
        put("rate_down", value);
    }

    public String getCategoryName() {
        return getString("category_name");
    }

    public void setCategoryName(String categoryName) {
        if (categoryName != null)
            put("category_name", categoryName);
    }

    public JSONArray getComments() {
        return getJSONArray("comments");
    }

    public void setComments(ArrayList<Comment> comments) {
        if (comments != null)
            addAll("comments", comments);
    }

    public void addComment(Comment comment) {
        if (comment != null)
            add("comments", comment);
    }
}
