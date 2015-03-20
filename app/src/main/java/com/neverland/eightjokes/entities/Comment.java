package com.neverland.eightjokes.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Vasilev on 15.1.2015 г..
 *
 * Comment is a entity class.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment(){

    }

    public String getAuthor() {
        return (String) get("author_name");
    }

    public void setAuthor(String authorName) {
        put("author_name", authorName);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }
}
