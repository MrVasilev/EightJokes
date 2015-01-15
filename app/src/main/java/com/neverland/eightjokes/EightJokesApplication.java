package com.neverland.eightjokes;

import android.app.Application;

import com.neverland.eightjokes.entities.Category;
import com.neverland.eightjokes.entities.Comment;
import com.neverland.eightjokes.entities.Joke;
import com.neverland.eightjokes.entities.User;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Vasilev on 15.1.2015 г..
 */
public class EightJokesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register all Parse objects
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Joke.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Category.class);

        //Init Parse
        Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
    }
}
