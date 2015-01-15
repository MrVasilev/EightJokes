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
}
