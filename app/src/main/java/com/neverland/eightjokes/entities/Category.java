package com.neverland.eightjokes.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Vasilev on 15.1.2015 г..
 *
 * Category is a entry class.
 */
@ParseClassName("Category")
public class Category extends ParseObject {

    public Category(){

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }
}
