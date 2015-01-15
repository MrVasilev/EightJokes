package com.neverland.eightjokes.entities;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import org.json.JSONArray;

/**
 * Created by Vasilev on 15.1.2015 г..
 *
 * User is a entity class.
 */
@ParseClassName("User")
public class User extends ParseUser {

    public User(){

    }

    public JSONArray getJokes() {
        return getJSONArray("jokes");
    }

    public void setJokes(JSONArray jokes) {
        put("jokes", jokes);
    }

    public void addJoke(Joke joke){

        JSONArray allJokes = getJokes();

        if(allJokes != null){
            allJokes.put(joke);
            setJokes(allJokes);
        }
    }
}
