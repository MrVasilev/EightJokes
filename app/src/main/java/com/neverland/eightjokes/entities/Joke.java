package com.neverland.eightjokes.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

/**
 * Created by Vasilev on 15.1.2015 г..
 *
 * Joke is a entry class.
 */
@ParseClassName("Joke")
public class Joke extends ParseObject {

    public Joke(){
        put("rate_up", 0);
        put("rate_down", 0);
    }

    public User getAuthor() {
        return (User) get("author");
    }

    public void setAuthor(User author) {
        put("author", author);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    public int getRateUp() {
        return getInt("rate_up");
    }

    public void incrementRateUp() {
        increment("rate_up");
    }

    public int getRateDown() {
        return getInt("rate_down");
    }

    public void setRateDown() {
        increment("rate_down");
    }

    public String getCategoryName() {
        return getString("category_name");
    }

    public void setCategoryName(String categoryName) {
        put("category_name", categoryName);
    }

    public JSONArray getComments(){
        return getJSONArray("comments");
    }

    public void setComments(JSONArray comments){
        put("comments", comments);
    }

    public void addComment(Comment comment){

        JSONArray allComments = getComments();

        if(allComments != null) {
            allComments.put(comment);
            setComments(allComments);
        }
    }
}
