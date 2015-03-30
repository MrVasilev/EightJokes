package com.neverland.eightjokes.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Vasilev on 15.1.2015 г..
 * <p/>
 * Comment is a entity class.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment() {

    }

    public String getAuthor() {
        return (String) get("author_name");
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

    public String getJokeId() {
        return (String) get("joke_id");
    }

    public void setJokeId(String jokeId) {
        if (jokeId != null)
            put("joke_id", jokeId);
    }
}
