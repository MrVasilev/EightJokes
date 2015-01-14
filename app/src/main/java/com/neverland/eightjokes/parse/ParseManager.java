package com.neverland.eightjokes.parse;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.neverland.eightjokes.Constants;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

/**
 * Created by Vasilev on 14.1.2015 г..
 *
 * ParseManager is a class which contains all Parse operations.
 * Parse is a cloud which give more options and potential.
 */
public class ParseManager {

    private static ParseManager instance;
    private Context context;

    private ParseManager(Context context){
        this.context = context;
    }

    public static ParseManager getInstance(Context context){

        if(instance == null)
            instance = new ParseManager(context);

        return instance;
    }

    public void initialize(){

        Parse.initialize(context, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
    }
}
